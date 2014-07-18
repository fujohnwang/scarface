package myactor

import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}
import java.util.concurrent.{ConcurrentHashMap, ForkJoinPool}



class MockActor(implicit scheduler: ForkJoinPool) extends Runnable {

  val messageSink = new AtomicReference[Any]

  val counter = new AtomicInteger()

  /**
   * CAUTION: this method may break when concurrently accessed
   */
  def !(message: String) {
    this.messageSink.set(message)
    scheduler.execute(this)
  }

  override def run(): Unit = {
    counter.incrementAndGet()
  }
}

object MyActors {
  def main(args: Array[String]) {

    implicit val scheduler = new ForkJoinPool(Runtime.getRuntime.availableProcessors() + 1, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true)
    try {
      val actorRegistry = new ConcurrentHashMap[String, MockActor]()

      actorRegistry.put("actor1", new MockActor)
      actorRegistry.put("actor2", new MockActor)
      actorRegistry.put("actor3", new MockActor)


      for (i <- (0 until 8)) {

        val actor = i % 3 match {
          case 0 => actorRegistry.get("actor1")
          case 1 => actorRegistry.get("actor2")
          case 2 => actorRegistry.get("actor3")
        }

        actor ! ""
      }

      import scala.collection.JavaConversions._

      for ((name, actor) <- actorRegistry) {
        println(s"actor:$name has counter=${actor.counter.get()}")
      }
    } finally {
      scheduler.shutdown()
    }
  }

}