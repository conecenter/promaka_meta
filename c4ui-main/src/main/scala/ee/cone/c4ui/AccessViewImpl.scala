package ee.cone.c4ui

import ee.cone.c4actor._
import ee.cone.c4proto.c4component
import ee.cone.c4vdom.{ChildPair, OfDiv}

case object AccessViewsKey extends SharedComponentKey[Map[String,AccessView[_]]]

@c4component("InnerAccessViewRegistryApp") class InnerAccessViewRegistry(accessViews: List[AccessView[_]]) extends ToInject {
  def toInject: List[Injectable] = AccessViewsKey.set(CheckedMap(accessViews.map(v => v.valueClass.getName -> v)))
}

object AccessViewRegistryImpl extends AccessViewRegistry {
  def view[P](access: Access[P]): Context=>List[ChildPair[OfDiv]] =
    local => AccessViewsKey.of(local)(access.initialValue.getClass.getName).asInstanceOf[AccessView[P]].view(access)(local)
}
