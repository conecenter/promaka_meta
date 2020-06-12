package ee.cone.c4vdom_impl

import ee.cone.c4vdom.Types.VDomKey
import ee.cone.c4vdom._

class SortTagsImpl(
  child: ChildPairFactory
) extends SortTags {
  def tBodyRoot[State](handler: SortHandler[State], items: List[ChildPair[OfDiv]]): ChildPair[OfDiv] =
    child[OfDiv]("body", TBodySortRoot(items.map(_.key), handler), items)
  def handle(key: VDomKey, item: ChildPair[OfDiv]): ChildPair[OfDiv] =
    child[OfDiv](key, SortHandle(), item::Nil)
}

case class SortHandle() extends VDomValue {
  def appendJson(builder: MutableJsonBuilder): Unit = {
    builder.startObject()
    builder.append("tp").append("SortHandle")
    builder.end()
  }
}

case class TBodySortRoot[State](value: List[VDomKey], handler: SortHandler[State]) extends VDomValue  with Receiver[State] {
  def appendJson(builder: MutableJsonBuilder): Unit = {
    builder.startObject()
    builder.append("tp").append("TBodySortRoot")
    builder.append("identity").append("ctx")
    builder.append("value").startArray();{
      value.foreach(builder.just.append)
      builder.end()
    }
    builder.end()
  }
  def receive: Handler = message => handler.handle(
    message.header("x-r-sort-obj-key"),
    (message.header("x-r-sort-order-0"), message.header("x-r-sort-order-1")),
  )
}


