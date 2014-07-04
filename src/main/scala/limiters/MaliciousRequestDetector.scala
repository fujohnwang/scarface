package limiters

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


case class Request(deviceId: String, userId: String)

trait MaliciousRequestDetector {
  def isMaliciousRequest(request: Request): (Boolean, String)
}

class WacaiMaliciousRequestDetector(timeThreshold: Long, rateLimit: Int) extends MaliciousRequestDetector {

  val requestRegistry = new java.util.concurrent.ConcurrentHashMap[Request, Long]
  val requestRateLimitCounters = new java.util.concurrent.ConcurrentHashMap[Request, AtomicInteger]()


  def isMaliciousRequest(request: Request): (Boolean, String) = {
    val oldTimestamp = requestRegistry.get(request)
    println(oldTimestamp)

    val newTimestamp = System.currentTimeMillis()
    requestRegistry.put(request, newTimestamp)

    if (newTimestamp - oldTimestamp < timeThreshold) {
      requestRateLimitCounters.putIfAbsent(request, new AtomicInteger(0))
      if (requestRateLimitCounters.get(request).incrementAndGet() > rateLimit) {
        return (true, "block for a day")
      }
      return (true, "drop the request")
    }


    (false, null)
  }
}


object Main {
  def main(args: Array[String]) {


    val detector = new WacaiMaliciousRequestDetector(2000, 3)

    val request = new Request("1", "wac")
    for (i <- (0 until 5)) {
      val (malicious, message) = detector.isMaliciousRequest(request)
      if (malicious) {
        println(s"current request:$request is possibly malicious, return message:$message to the client")
      }
      else {
        println(s"ok for $i")
      }
    }


    //    val m = new java.util.concurrent.ConcurrentHashMap[Request, Long]
    //
    //    val s = m.putIfAbsent(request, System.currentTimeMillis())
    //
    //    println(s)
    //
    //    TimeUnit.SECONDS.sleep(1)
    //
    //    val s2  = m.putIfAbsent(request, System.currentTimeMillis())
    //    println(s2)
  }
}

