package com.github.fujohnwang.scarface.concurrency

import concurrent._
import duration._
import java.util.concurrent.{TimeoutException, TimeUnit}

object Futures {
  def main(args: Array[String]) {
    import ExecutionContext.Implicits.global

    //    val f = future {
    //      TimeUnit.SECONDS.sleep(1)
    //      1
    //    }
    //    f.onSuccess {
    //      case i: Int => println("result receives")
    //    }
    //
    //    println("wait for complete, but may be executed before the callback.")
    //
    //    TimeUnit.SECONDS.sleep(2)


    val f = future {
      println("raw computing")
      throw new Exception()

    } recoverWith {
      case _ => future {
        println("recover with ")
        2
      }
    }


    Await.result(f, 5 minute)
  }
}