package ee.cone.c4actor

import java.time.Instant

import com.typesafe.scalalogging.LazyLogging
import ee.cone.c4actor.Types.{SrcId, TransientMap}
import ee.cone.c4assemble.Types._
import ee.cone.c4di.c4

import scala.collection.immutable.{Map, Seq}
import scala.util.control.NonFatal
import scala.util.{Success, Try}

trait TxTransforms {
  def get(global: RichContext): Map[SrcId,TransientMap=>TransientMap]
}

@c4("ServerCompApp") final class DefLongTxWarnPeriod extends LongTxWarnPeriod(Option(System.getenv("C4TX_WARN_PERIOD_MS")).fold(500L)(_.toLong))

@c4("ServerCompApp") final class TxTransformsImpl(
  qMessages: QMessages, warnPeriod: LongTxWarnPeriod, catchNonFatal: CatchNonFatal,
  getTxTransform: GetByPK[TxTransform],
) extends TxTransforms with LazyLogging {
  def get(global: RichContext): Map[SrcId,TransientMap=>TransientMap] =
    getTxTransform.ofA(global).keys.map(k=>k->handle(global,k)).toMap
  private def handle(global: RichContext, key: SrcId): TransientMap=>TransientMap = {
    val enqueueTimer = NanoTimer()
    prev =>
    val startLatency = enqueueTimer.ms
    if(startLatency > 200)
      logger.debug(s"tx $key start latency $startLatency ms")
    if( //todo implement skip for outdated world
      global.offset < InnerReadAfterWriteOffsetKey.of(prev) ||
      Instant.now.isBefore(InnerSleepUntilKey.of(prev))
    ) prev else localizeThreadName(setName=>doHandle(global,key,setName,prev))
  }

  private def localizeThreadName[T](f: (String=>Unit)=>T): T = {
    val thread = Thread.currentThread
    val was = thread.getName
    try f(thread.setName) finally thread.setName(was)
  }

  private def doHandle(global: RichContext, key: SrcId, setName: String=>Unit, prev: TransientMap): TransientMap =
    catchNonFatal {
        getTxTransform.ofA(global).get(key) match {
          case None => prev
          case Some(tr) =>
            val workTimer = NanoTimer()
            val name = s"${tr.getClass.getName}-$key"
            setName(s"tx-from-${System.currentTimeMillis}-$name")
            val prepLocal = new Context(global.injected, global.assembled, global.executionContext, prev)
            val transformedLocal = TxTransformOrigMeta(tr.getClass.getName).andThen(tr.transform)(prepLocal)
            val transformPeriod = workTimer.ms
            val nextLocal = qMessages.send(transformedLocal)
            val period = workTimer.ms
            if(period > warnPeriod.value)
              logger.warn(s"tx $name worked for $period ms (transform $transformPeriod ms)")
            nextLocal.transient
        }
    }(s"Tx failed [$key][${Thread.currentThread.getName}]"){ e =>
        val was = InnerErrorKey.of(prev)
        val exception = e match {
          case e: Exception => e
          case err => new Exception(err)
        }
        Function.chain(List(
          InnerErrorKey.set(exception :: was),
          InnerSleepUntilKey.set(Instant.now.plusSeconds(was.size))
        ))(Map.empty)
    }
}

case object InnerErrorKey extends InnerTransientLens(ErrorKey)
case object InnerSleepUntilKey extends InnerTransientLens(SleepUntilKey)
case object InnerReadAfterWriteOffsetKey extends InnerTransientLens(ReadAfterWriteOffsetKey)

abstract class InnerTransientLens[Item](key: TransientLens[Item]) extends AbstractLens[TransientMap,Item] with Product {
  def of: TransientMap => Item =
    m => m.getOrElse(key, key.default).asInstanceOf[Item]
  def set: Item => TransientMap=>TransientMap =
    value => m => m + (key -> value.asInstanceOf[Object])
}

class TxObserver(val value: Observer[RichContext])

@c4("SerialObserversApp") final class SerialTxObserver(
  transforms: TxTransforms
) extends TxObserver(new SerialObserver(Map.empty)(transforms))

@c4("ParallelObserversApp") final class ParallelTxObserver(
  transforms: TxTransforms,
  execution: Execution
) extends TxObserver(new ParallelObserver(Map.empty,transforms,execution))

class SerialObserver(localStates: Map[SrcId,TransientMap])(
  transforms: TxTransforms
) extends Observer[RichContext] {
  def activate(global: RichContext): Observer[RichContext] = {
    val nLocalStates = transforms.get(global).transform{ case(key,handle) =>
      handle(localStates.getOrElse(key,Map.empty))
    }
    new SerialObserver(nLocalStates)(transforms)
  }
}

class ParallelObserver(
  localStates: Map[SrcId,SkippingFuture[TransientMap]],
  transforms: TxTransforms,
  execution: Execution
) extends Observer[RichContext] with LazyLogging {
  private def empty: SkippingFuture[TransientMap] = execution.skippingFuture(Map.empty)
  def activate(global: RichContext): Observer[RichContext] = {
    val inProgressMap = localStates.filter{ case(k,v) =>
      v.value match {
        case None => true // inProgress
        case Some(Success(transient)) =>
          global.offset < InnerReadAfterWriteOffsetKey.of(transient)
        case a => throw new Exception(s"$a")
      }
    }
    val toAdd = transforms.get(global).transform{ case(key,handle) =>
      localStates.getOrElse(key,empty).map(handle)
    }
    val nLocalStates = inProgressMap ++ toAdd
    logger.debug(
      s"txTr count: ${nLocalStates.size}, " +
      s"inProgress: ${inProgressMap.size}, " +
      s"uncompleted: ${inProgressMap.values.count(_.value.isEmpty)}, " +
      s"just-mapped: ${toAdd.size}"
    )
    new ParallelObserver(nLocalStates,transforms,execution)
  }
}

/*
finished ok started deferred
None => keep
Some(Success(Some(local))) if global < local => keep
Some(Success(Some(local))) => not keep
Some(Failure(err)) => exit

 */

/* todo world provider?
world
world changed
all jobs
job finished(offset)
 */


/*
* in trans? in was? !isDone?
*  0 0 x => -
*  0 1 0 => del
*  0 1 1 => keep same
*  1 0 x => new
*  1 1 x => chain
* */