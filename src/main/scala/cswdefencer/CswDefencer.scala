package cswdefencer

trait CswDefencer[T] {
  def predicate(input: T): DefenceStrategy
}

object Main {
  def main(args: Array[String]) {
    val defencer = new CswDefencer[String] {
      def predicate(input: String): DefenceStrategy = {
        DefenceStrategy.IGNORE
      }
    }

    defencer.predicate("request context") match {
      case DefenceStrategy.IGNORE => // continue
      case DefenceStrategy.WARN => // response with warning response
      case DefenceStrategy.PUNISH => // response with punish response
    }
  }
}








