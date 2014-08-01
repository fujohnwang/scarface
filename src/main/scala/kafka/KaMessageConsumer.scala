package kafka


import java.util.Properties

import kafka.consumer._
import kafka.serializer.{Decoder, StringEncoder}

object KaMessageConsumer {

  val topic = "test"

  def main(args: Array[String]) {
    val props = new Properties()
    props.put("zookeeper.connect", "localhost:2181")
    props.put("group.id", "testConsumer0")
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    props.put("auto.commit.interval.ms", "1000")
    val consumerConfig = new ConsumerConfig(props)

    val consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig)
    try {
      val topicCountMap: java.util.Map[String, Integer] = new java.util.HashMap[String, Integer]
      topicCountMap.put(topic, 1) // 1 thread to consume in serial
      val streams: java.util.List[KafkaStream[String, String]] = consumerConnector.createMessageStreamsByFilter(new Whitelist("test"), 1, new Decoder[String] {
          override def fromBytes(bytes: Array[Byte]): String = new String(bytes, "UTF-8")
        }, new Decoder[String] {
          override def fromBytes(bytes: Array[Byte]): String = new String(bytes, "UTF-8")
        })
      val stream = streams.get(0)
      val messageIterator = stream.iterator()
      while (messageIterator.hasNext()) {
        val message = messageIterator.next()
        println(s"${message.key()} - ${message.message()}")
      }
    } finally {
      consumerConnector.shutdown()
    }
  }
}