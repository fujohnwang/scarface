package mysdal.routes

import org.mvel2.MVEL

trait ExpressionEngine {
  def eval(expression: String, context: AnyRef): Boolean
}


class MVELExpressionEngine extends ExpressionEngine {
  def eval(expression: String, context: AnyRef): Boolean = MVEL.evalToBoolean(expression, context)
}

