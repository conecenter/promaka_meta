
package ee.cone.c4actor

import ee.cone.c4di.{c4, provide}

@c4("RichDataCompApp") final class ModelConditionFactoryProvider {
  @provide def get: Seq[ModelConditionFactory[Unit]] = List(new ModelConditionFactoryImpl[Unit])
}

class ModelConditionFactoryImpl[Model] extends ModelConditionFactory[Model] {
  def of[OtherModel]: ModelConditionFactory[OtherModel] =
    new ModelConditionFactoryImpl[OtherModel]

  def ofWithCl[OtherModel]: Class[OtherModel] => ModelConditionFactory[OtherModel] = cl =>
    new ModelConditionFactoryImpl[OtherModel]

  def intersect: (Condition[Model], Condition[Model]) => Condition[Model] = {
    case (AnyCondition(), left) => left
    case (right, AnyCondition()) => right
    case (left, right) => IntersectCondition(left, right)
  }

  def union: (Condition[Model], Condition[Model]) => Condition[Model] = {
    case (AnyCondition(), _) => AnyCondition()
    case (_, AnyCondition()) => AnyCondition()
    case (left, right) => UnionCondition(left, right)
  }

  def any: Condition[Model] =
    AnyCondition()

  def leaf[By <: Product, Field](lens: ProdGetter[Model, Field], by: By, byOptions: List[AbstractMetaAttr])(
    implicit check: ConditionCheck[By, Field]
  ): Condition[Model] = {
    val preparedBy = check.prepare(byOptions)(by)
    if (check.defaultBy.exists(default => default(preparedBy)))
      AnyCondition()
    else
      ProdConditionImpl(filterMetaList(lens), preparedBy)(check.check(preparedBy), lens.of)
  }

  def filterMetaList[Field]: ProdGetter[Model, Field] => List[AbstractMetaAttr] =
    _.metaList.collect { case l: NameMetaAttr => l }
}

case class ProdConditionImpl[By <: Product, Model, Field](
  metaList: List[AbstractMetaAttr], by: By
)(
  fieldCheck: Field => Boolean, of: Model => Field
) extends ProdCondition[By, Model] {
  def check(model: Model): Boolean = fieldCheck(of(model))
}

case class IntersectCondition[Model](
  left: Condition[Model],
  right: Condition[Model]
) extends Condition[Model] {
  def check(line: Model): Boolean = left.check(line) && right.check(line)
}

case class UnionCondition[Model](
  left: Condition[Model],
  right: Condition[Model]
) extends Condition[Model] {
  def check(line: Model): Boolean = left.check(line) || right.check(line)
}

case class AnyCondition[Model]() extends Condition[Model] {
  def check(line: Model): Boolean = true
}
