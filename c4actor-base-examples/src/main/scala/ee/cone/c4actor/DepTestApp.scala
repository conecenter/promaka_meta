package ee.cone.c4actor

import java.nio.ByteBuffer
import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import ee.cone.c4actor.CtxType.{Ctx, Request}
import ee.cone.c4actor.DepDraft._
import ee.cone.c4actor.LULProtocol.{LULNode, PffNode}
import ee.cone.c4actor.TestRequests.RootDepRequest
import ee.cone.c4actor.Types.SrcId
import ee.cone.c4actor.dependancy._
import ee.cone.c4assemble.Types.Values
import ee.cone.c4assemble.{Assemble, assemble, by, was}
import ee.cone.c4proto.{Id, Protocol, protocol}


@protocol object LULProtocol extends Protocol {

  @Id(0x0001) case class LULNode(@Id(0x0003) srcId: String, @Id(0x0005) parentId: String)

  @Id(0x0010) case class PffNode(@Id(0x0013) srcId: String, @Id(0x0015) value: Int)

}

//  C4STATE_TOPIC_PREFIX=ee.cone.c4actor.DepTestApp sbt ~'c4actor-base-examples/runMain ee.cone.c4actor.ServerMain'
@assemble class DepAssemble(handlerRegistry: RequestHandlerRegistry, adapterRegistry: QAdapterRegistry) extends Assemble {

  def sparkJoiner
  (
    key: SrcId,
    nodes: Values[LULNode]
  ): Values[(SrcId, RequestWithSrcId)] =
    for (
      node ← nodes
    ) yield {
      //println("Root rq Handle")
      WithPK(RequestWithSrcId("root", RootDepRequest("kek")))
    }

  type ToResponse = SrcId

  def reponsesTest
  (
    key: SrcId,
    @by[ToResponse] responses: Values[Response]
  ): Values[(SrcId, Request)] = {
    //println()
    //println(s"Responses: $key:${responses.head}")
    Nil
  }

  def RQtoURSO
  (
    key: SrcId,
    @was requests: Values[RequestWithSrcId],
    @was @by[ToResponse] responses: Values[Response]
  ): Values[(SrcId, UpResolvable)] =
    for (
      request ← requests;
      dep ← handlerRegistry.handle(request.request)
    ) yield {
      //println()
      //println(s"RQ $key = $request")
      val ctx: Ctx = buildContext(responses)
      //println(s"CTX = $responses")
      //println(s"CTX $key = $ctx")
      //println(s"UpResolvable $key = ${dep.asInstanceOf[InnerDep[_]].resolve(ctx)}")
      WithPK(UpResolvable(request, dep.asInstanceOf[InnerDep[_]].resolve(ctx)))
    }

  def URSOtoRequest
  (
    key: SrcId,
    resolvable: Values[UpResolvable]
  ): Values[(SrcId, RequestWithSrcId)] =
    for (
      rs ← resolvable;
      rq ← rs.resolvable.requests
    ) yield {
      //println()
      //println(s"RSOtoRequest $key= ${rq.addParent(rs.request.srcId)}")
      val valueAdapter = adapterRegistry.byName(rq.getClass.getName)
      val bytes = valueAdapter.encode(rq)
      val id = UUID.nameUUIDFromBytes(toBytes(valueAdapter.id) ++ bytes).toString
      WithPK(RequestWithSrcId(id, rq).addParent(rs.request.srcId))
    }

  def URSOtoResponse
  (
    key: SrcId,
    upResolvable: Values[UpResolvable]
  ): Values[(ToResponse, Response)] =
    upResolvable.flatMap { upRes ⇒
      //println()
      val response = Response(upRes.request, upRes.resolvable.value, upRes.request.parentSrcIds)
      //println(s"Resp: $response")
      WithPK(response) ::
        (for (srcId ← response.rqList) yield (srcId, response))
    }

  def UnresolvedDeps
  (
    key: SrcId,
    @was requests: Values[RequestWithSrcId],
    @was resolvables: Values[UpResolvable]
  ): Values[(SrcId, UnresolvedDep)] =
    for (
      rq ← requests;
      resv ← resolvables
      if resv.resolvable.value.isEmpty
    ) yield {
      //println(s"UnRes $rq:${resv.resolvable}")
      WithPK(UnresolvedDep(rq, resv))
    }

}

case class UnresolvedDep(rq: RequestWithSrcId, resolvable: UpResolvable)

class DepTestStart(
  execution: Execution, toUpdate: ToUpdate, contextFactory: ContextFactory
) extends Executable with LazyLogging {
  def run() = {
    import LEvent.update

    val recs = update(LULNode("1", "")) ++ update(PffNode("123", 239)) ++ update(PffNode("124", 666))
    /*
          update(Node("12","1")) ++ update(Node("13","1")) ++
          update(Node("124","12")) ++ update(Node("125","12"))*/
    val updates = recs.map(rec ⇒ toUpdate.toUpdate(rec)).toList
    val context = contextFactory.create()
    val nGlobal = ReadModelAddKey.of(context)(updates)(context)

    //logger.info(s"${nGlobal.assembled}")
    logger.debug("asddfasdasdasdas")
    println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    println(s"Final result: ${ByPK(classOf[UpResolvable]).of(nGlobal)("root").resolvable.value}")
    println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    println(s"Unresolved: \n${ByPK(classOf[UnresolvedDep]).of(nGlobal).toList.mkString("\n")}")
    println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    execution.complete()

    /*
    Map(
      ByPK(classOf[PCProtocol.RawParentNode]) -> Map(
        "1" -> RawParentNode("1","P-1")
      ),
      ByPK(classOf[PCProtocol.RawChildNode]) -> Map(
        "2" -> RawChildNode("2","1","C-2"),
        "3" -> RawChildNode("3","1","C-3")
      ),
      ByPK(classOf[ParentNodeWithChildren]) -> Map(
        "1" -> ParentNodeWithChildren("1",
          "P-1",
          List(RawChildNode("2","1","C-2"), RawChildNode("3","1","C-3"))
        )
      )
    ).foreach{
      case (k,v) ⇒ assert(k.of(nGlobal).toMap==v)
    }*/
  }
}

class DepTestApp extends RichDataApp
  with ExecutableApp
  with VMExecutionApp
  with TreeIndexValueMergerFactoryApp
  with SimpleAssembleProfilerApp
  with ToStartApp
  with RqHandlerRegistryImplApp
  with ByPKRequestHandlerApp {
  override def handledClasses: List[Class[_]] = classOf[PffNode] :: super.handledClasses

  override def handlers: List[RequestHandler[_]] = FooRequestHandler :: RootRequestHandler :: super.handlers

  override def protocols: List[Protocol] = LULProtocol :: TestRequests:: super.protocols

  override def assembles: List[Assemble] = new DepAssemble(handlerRegistry, QAdapterRegistryFactory(protocols)) :: super.assembles

  override def toStart: List[Executable] = new DepTestStart(execution, toUpdate, contextFactory) :: super.toStart
}