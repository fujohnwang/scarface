package kafka


import java.util
import java.util.Collections

import kafka.cluster.Broker
import kafka.javaapi.{TopicMetadataRequest, PartitionMetadata}
import kafka.javaapi.consumer.SimpleConsumer
import org.slf4j.LoggerFactory

import scala.beans.BeanProperty
import collection.JavaConversions._

trait MessageProcessor[K, V] {
  def onMessage(topic: String, partition: Int, key: K, message: V): Unit
}

case class BrokerAddress(address: String, port: Int = 9092)

class TextMessageConsumer(seeds: Seq[BrokerAddress], clientId: String, topic: String, partition: Int = 0) {

  val logger = LoggerFactory.getLogger("TextMessageConsumer")

  @BeanProperty
  var soTimeout: Int = 10000
  @BeanProperty
  var bufferSize: Int = 1024 * 64

  protected val replicas: java.util.List[Broker] = new util.ArrayList[Broker]()

  protected def findLeader(): PartitionMetadata = {
    val topics = Collections.singletonList(topic)
    for (seed <- seeds) {
      var consumer: SimpleConsumer = null
      try {
        consumer = new SimpleConsumer(seed.address, seed.port, soTimeout, bufferSize, "leaderFinderFor" + clientId)
        val request = new TopicMetadataRequest(topics)
        val response = consumer.send(request)
        for (topicMetadata <- response.topicsMetadata) {
          for (partitionMetadata <- topicMetadata.partitionsMetadata) {
            if (partitionMetadata.partitionId == partition) {
              replicas.addAll(partitionMetadata.replicas) // fetch a whole broker list instead of just seed broker(s)
              return partitionMetadata
            }
          }
        }
      } catch {
        case ex: Exception => logger.error("Error communicating with Broker [" + seed + "] to find Leader for [" + topic
          + ", " + partition + "] Reason: " + ex)
      } finally {
        if (consumer != null) consumer.close()
      }
    }
    null
  }

  /**
   * 只有第一次才需要， 之后， 应用端应该记录上次消费的数据的offset
   *
   * @param topic
   * @param partition
   * @return
   */
  protected def getLastOffsetOf(topic: String, partition: Int = 0): Long = {


    0L
  }
}