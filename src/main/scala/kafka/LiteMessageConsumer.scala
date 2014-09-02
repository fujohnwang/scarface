package kafka

import scala.beans.BeanProperty

sealed trait MessagePosition

case object EarliestPosition extends MessagePosition

case object LatestPosition extends MessagePosition

case class StaticPosition(position: Long) extends MessagePosition

/**
 * a MessageDispatcher will be executed in single thread by LiteMessageConsumer,
 * but it can dispatch messages concurrently or in parallel asynchronously.
 *
 * of course, if sequential message processing is enough, a MessageDispatcher can be used as message handler directly too.
 */
trait MessageDispatcher[K, V] {
  def onMessage(topic: String, partition: Int, key: K, message: V): Unit
}


/**
 * A LiteMessageConsumer will ONLY consume message from partition 0 of any topic it subscribe from.
 *
 * We would like to simplify kafka's message producer/consumer model by degrading the topic and partition composition structure.
 */
class LiteMessageConsumer[K, V](brokers: String, topic: String, messageProcessor: MessageDispatcher[K, V]) {

  val partition: Int = 0

  @BeanProperty
  var startPosition: MessagePosition = LatestPosition

  @BeanProperty
  var clientId: String = _

  @BeanProperty
  var soTimeout: Int = 10000
  @BeanProperty
  var bufferSize: Int = 1024 * 64

  /**
   * if start position assigned is out of range on the broker, fails fast by throwing exception and exit.
   * recovery exceptions can be caught and handled property, as to other kind of exceptions, just let it fail fast and exit.
   */
  def start(): Unit = {
    // kick off the consumer and dispatch message received to message processor
  }
}


object LiteMessageConsumer {
  def main(args: Array[String]) {
    val messageConsumer = new LiteMessageConsumer[String, String]("localhost:9092", "test_topic", new MessageDispatcher[String, String] {
      override def onMessage(topic: String, partition: Int, key: String, message: String): Unit = ???
    })

    messageConsumer.start() // handle exceptions if needs
  }
}