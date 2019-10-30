package ee.cone.c4gate

import ee.cone.c4actor._
import ee.cone.c4assemble.fieldAccess
import ee.cone.c4gate.CommonFilterProtocol.{B_Contains, B_DateBefore}
import ee.cone.c4proto.{Id, c4, protocol}
import ee.cone.c4ui.AccessView
import ee.cone.c4vdom.{ChildPair, OfDiv}

//todo access Meta from here, ex. precision
//todo ask if predicate active ?Option[Field=>Boolean]

//// api

@protocol("TestTodoApp") object CommonFilterProtocolBase   {
  @Id(0x0006) case class B_DateBefore(
    @Id(0x0001) srcId: String,
    @Id(0x0002) value: Option[Long]
  )
  @Id(0x0007) case class B_Contains(
    @Id(0x0001) srcId: String,
    @Id(0x0002) value: String
  )
}

trait CommonFilterConditionChecks {
  implicit def dateBefore: ConditionCheck[B_DateBefore,Long]
  implicit def contains: ConditionCheck[B_Contains,String]
}

//// impl

@c4("CommonFilterApp") class DateBeforeDefault extends DefaultModelFactory(classOf[B_DateBefore],B_DateBefore(_,None))
@c4("CommonFilterApp") class ContainsDefault extends DefaultModelFactory(classOf[B_Contains],B_Contains(_,""))

case object DateBeforeCheck extends ConditionCheck[B_DateBefore,Long] {
  def prepare: List[AbstractMetaAttr] => B_DateBefore => B_DateBefore = _ => identity[B_DateBefore]
  def check: B_DateBefore => Long => Boolean = by => value => by.value forall (_>value)

  def defaultBy: Option[B_DateBefore => Boolean] = None
}

case object ContainsCheck extends ConditionCheck[B_Contains,String] {
  def prepare: List[AbstractMetaAttr] => B_Contains => B_Contains = _ => identity[B_Contains]
  def check: B_Contains => String => Boolean = by => value => value contains by.value

  def defaultBy: Option[B_Contains => Boolean] = None
}

@c4("CommonFilterApp") class CommonFilterConditionChecksImpl extends CommonFilterConditionChecks {
  lazy val dateBefore: ConditionCheck[B_DateBefore,Long] = DateBeforeCheck
  lazy val contains: ConditionCheck[B_Contains,String] = ContainsCheck
}

@c4("CommonFilterApp") class DateBeforeAccessView(testTags: TestTags[Context]) extends AccessView(classOf[B_DateBefore]) {
  def view(access: Access[B_DateBefore]): Context=>List[ChildPair[OfDiv]] =
    local => List(testTags.dateInput(access to CommonFilterAccess.dateBeforeValue))
}

@c4("CommonFilterApp") class ContainsAccessView(testTags: TestTags[Context]) extends AccessView(classOf[B_Contains]) {
  def view(access: Access[B_Contains]): Context=>List[ChildPair[OfDiv]] =
    local => List(testTags.input(access to CommonFilterAccess.containsValue))
}

@fieldAccess object CommonFilterAccessBase {
  lazy val dateBeforeValue: ProdLens[B_DateBefore,Option[Long]] = ProdLens.of(_.value)
  lazy val containsValue: ProdLens[B_Contains,String] = ProdLens.of(_.value)
}


