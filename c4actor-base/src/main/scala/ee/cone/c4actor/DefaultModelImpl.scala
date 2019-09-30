package ee.cone.c4actor

import ee.cone.c4proto.c4component

@c4component("RichDataAutoApp") class DefaultModelRegistryImpl(
  defaultModelFactories: List[DefaultModelFactory[_]]
)(
  val reg: Map[String,DefaultModelFactory[_]] =
    CheckedMap(defaultModelFactories.map(f=>f.valueClass.getName->f))
) extends DefaultModelRegistry {
  def get[P<:Product](className: String): DefaultModelFactory[P] =
    reg(className).asInstanceOf[DefaultModelFactory[P]]
}
