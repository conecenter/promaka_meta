package ee.cone.c4actor.dep.request

import ee.cone.c4actor.Types.SrcId
import ee.cone.c4actor.dep._
import ee.cone.c4actor.dep.request.ByClassNameAllRequestProtocol.N_ByClassNameAllRequest
import ee.cone.c4actor.{AssembleName, AssemblesApp, WithPK}
import ee.cone.c4assemble.Types.{Each, Values}
import ee.cone.c4assemble._
import ee.cone.c4proto.{Id, protocol}

case class ByClassNameAllAskImpl(depFactory: DepFactory) extends ByClassNameAllAsk {
  def askByClAll[A <: Product](cl: Class[A]): Dep[List[A]] = depFactory.uncheckedRequestDep[List[A]](N_ByClassNameAllRequest(cl.getName))
}

trait ByClassNameAllAsk {
  def askByClAll[A <: Product](cl: Class[A]): Dep[List[A]]
}

trait ByClassNameRequestMix extends DepFactoryApp with ByClassNameRequestApp {
  override def byClassNameAllAsk: ByClassNameAllAsk = ByClassNameAllAskImpl(depFactory)
}

trait ByClassNameRequestApp {
  def byClNameAllClasses: List[Class[_ <: Product]] = Nil

  def byClassNameAllAsk: ByClassNameAllAsk
}

trait ByClassNameAllRequestHandlerAppBase extends AssemblesApp with ByClassNameRequestApp with DepResponseFactoryApp {
  override def assembles: List[Assemble] =
    byClNameAllClasses
      .map(cl => cl.getName -> cl).groupBy(_._1).values.map(_.head._2).toList
      .map(cl => new ByClassNameAllRequestGenericHandler(cl, depResponseFactory)) ::: super.assembles
}

@assemble class ByClassNameAllRequestGenericHandlerBase[Model <: Product](modelCl: Class[Model], util: DepResponseFactory)
  extends AssembleName("ByClassNameAllRequestGenericHandler", modelCl) {
  type ByClassNameRequestAll = AbstractAll

  def GatherAllModels(
    modelId: SrcId,
    model: Each[Model]
  ): Values[(ByClassNameRequestAll, Model)] = List(All -> model)

  def HandleRequest(
    requestId: SrcId,
    rq: Each[DepInnerRequest],
    @byEq[ByClassNameRequestAll](All) models: Values[Model]
  ): Values[(SrcId, DepResponse)] =
    rq.request match {
      case request: N_ByClassNameAllRequest if request.className == modelCl.getName =>
        List(WithPK(util.wrap(rq, Option(models.toList))))
      case _ => Nil
    }
}

@protocol("ByClassNameAllRequestHandlerApp") object ByClassNameAllRequestProtocol {

  @Id(0x0230) case class N_ByClassNameAllRequest(
    @Id(0x0231) className: String
  )

}
