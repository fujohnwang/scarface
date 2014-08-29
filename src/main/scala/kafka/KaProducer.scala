package kafka

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}
import org.springframework.beans.factory.{DisposableBean, InitializingBean, FactoryBean}

import scala.beans.BeanProperty

/**
 * 因为我们发送的KeyedMessage， Key和Value都是String，所以，原则上需要为Key和Value都指定序列化类，
 * 但如果不指定key的序列化类，默认会使用Value的，所以，我们只要指定Value的序列化类即可。
 */
object KaProducer {
  def main(args: Array[String]) {
    val topic = "csw_nbk_data"

    val properties = new Properties
    properties.put("metadata.broker.list", "192.168.1.209:9092")
    //    properties.put("key.serializer.class", "kafka.serializer.StringEncoder")
    properties.put("serializer.class", "kafka.serializer.StringEncoder") // 默认的序列化类只支持序列化byte[],所以，对string类型，要提供支持string的序列化类
    properties.put("request.required.acks", "-1") // -1指定同步到所有节点，牺牲性能获取数据可靠性

    val producerConfig = new ProducerConfig(properties)
    val producer = new Producer[String, String](producerConfig)
    try {
      val message = new KeyedMessage[String, String](topic, "12345", "马来西亚2")
      producer.send(message)
    } finally {
      producer.close
    }
  }
}


trait Messenger {
  def send(topic: String, message: String): Unit

  def send(topic: String, messageKey: String, messageBody: String): Unit
}

class KafkaMessenger(producer: Producer[String, String]) extends Messenger {
  override def send(topic: String, message: String): Unit = send(topic, null, message)

  override def send(topic: String, messageKey: String, messageBody: String): Unit = {
    val message = new KeyedMessage[String, String](topic, messageKey, messageBody)
    producer.send(message)
  }
}


/**
 * If a more fine-tuned Kafka Producer is needed, subclass this one to make yourself at ease.
 */
class KafkaMessengerFactoryBean extends FactoryBean[Messenger] with InitializingBean with DisposableBean {
  @BeanProperty
  var syncMode: Int = -1
  @BeanProperty
  var brokerList: String = _

  protected var messenger: Messenger = _
  protected var producer: Producer[String, String] = _

  override def getObject: Messenger = if (messenger != null) messenger else throw new IllegalStateException("messenger is not initialized yet, call afterPropertiesSet() first.")

  override def getObjectType: Class[_] = classOf[Messenger]

  override def isSingleton: Boolean = true

  override def afterPropertiesSet(): Unit = {
    if (brokerList == null || brokerList.trim.length == 0) throw new IllegalArgumentException("brokerList property must be given.")
    val prop = new Properties
    prop.put("metadata.broker.list", brokerList)
    prop.put("serializer.class", "kafka.serializer.StringEncoder")
    prop.put("request.required.acks", syncMode.toString)
    producer = new Producer[String, String](new ProducerConfig(prop))
    messenger = new KafkaMessenger(producer)
  }

  override def destroy(): Unit = {
    if (producer != null) {
      producer.close
      producer = null
    }
  }
}
