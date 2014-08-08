package kafka

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}

/**
 * 因为我们发送的KeyedMessage， Key和Value都是String，所以，原则上需要为Key和Value都指定序列化类，
 * 但如果不指定key的序列化类，默认会使用Value的，所以，我们只要指定Value的序列化类即可。
 */
object KaProducer {
  def main(args: Array[String]) {
    val topic = "pages"

    val properties = new Properties
    properties.put("metadata.broker.list", "192.168.1.209:9092")
//    properties.put("key.serializer.class", "kafka.serializer.StringEncoder")
    properties.put("serializer.class", "kafka.serializer.StringEncoder") // 默认的序列化类只支持序列化byte[],所以，对string类型，要提供支持string的序列化类
    properties.put("request.required.acks", "-1")  // -1指定同步到所有节点，牺牲性能获取数据可靠性

    val producerConfig = new ProducerConfig(properties)
    val producer = new Producer[String, String](producerConfig)
    try {
      val message = new KeyedMessage[String, String](topic, "message Key", "message content")
      producer.send(message)
    } finally {
      producer.close
    }
  }
}