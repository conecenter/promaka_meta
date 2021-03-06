package ee.cone.c4gate

import com.typesafe.scalalogging.LazyLogging
import ee.cone.c4actor.Types.SrcId
import ee.cone.c4actor._
import ee.cone.c4actor.QProtocol.S_Firstborn
import ee.cone.c4assemble.Types.{Each, Values}
import ee.cone.c4assemble._
import ee.cone.c4di.c4multi
import ee.cone.c4proto.ToByteString

@c4assemble("HiRateTxApp") class HiRateAssembleBase(factory: HiRateTxFactory) {
  def joinPosts(
    key: SrcId,
    firstborn: Each[S_Firstborn]
  ): Values[(SrcId, TxTransform)] = List(WithPK(factory.create("HiRateTx")))
}

@c4multi("HiRateTxApp") final case class HiRateTx(srcId: SrcId)(
  publisher: Publisher,
  txAdd: LTxAdd,
) extends TxTransform with LazyLogging {
  def transform(local: Context): Context = {
    val timeStr = System.currentTimeMillis.toString
    logger.info(s"start handling $timeStr")
    val bytes = ToByteString(timeStr)
    txAdd.add(publisher.publish(ByPathHttpPublication("/time",Nil,bytes),_+1000*60))(local)
  }
}

/*
@assemble class HiRateAssemble {
  def joinPosts(
    key: SrcId,
    post: Each[HttpPost]
  ): Values[(SrcId, TxTransform)] =
    List(WithPK(TestDelayHttpPostHandler(post.srcId, post)))
}

case class HiRateTx(srcId: SrcId, post: HttpPost) extends TxTransform with LazyLogging {
  def transform(local: Context): Context = {
    logger.info(s"start handling $srcId")
    concurrent.blocking{
      Thread.sleep(1000)
    }
    logger.info(s"finish handling $srcId")
    txAdd.add(delete(post))(local)
  }
}*/
