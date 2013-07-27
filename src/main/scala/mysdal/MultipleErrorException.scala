package mysdal

import java.util
import collection.JavaConversions._

class MultipleErrorException(exceptions: util.List[Throwable] = new util.ArrayList[Throwable]()) extends Throwable {
  def add(exception: Throwable) = exceptions.add(exception)

  override def toString() = exceptions.map(_.toString).mkString("\r\n\r\n")
}
