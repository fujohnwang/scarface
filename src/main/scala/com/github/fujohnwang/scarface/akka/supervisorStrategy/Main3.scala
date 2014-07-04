package com.github.fujohnwang.scarface.akka.supervisorStrategy

import akka.actor._

/**
 * 在当前Actor里定义supervisorStrategy只是相当于catch在当前Actor中创建的所有子actors所抛出的exceptions，
 * 但对当前Actor中抛出的Exception无效， 如果想在当前Actor中自己处理预期的异常， 则还是自己try catch比较好！
 * 非预期的Exception就随他去吧，让上层的supervisor actor的supervisorStrategy来处理。
 */
class UnstableActor extends Actor with ActorLogging {
  var counter: Long = 0

//  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
//    case _: UnstableException => SupervisorStrategy.Resume
//    case _ => SupervisorStrategy.Stop
//  }

  override def receive: Receive = {
    case Greeter.Greet => {
      counter = counter + 1
      try {
        if (counter % 2 == 0) {
          throw new UnstableException()
        } else {
          println(s"seems good with counter = $counter")
        }
      } catch {
        case e: UnstableException => println(e)
      }
    }
    case Greeter.Done => {
      println("shutdown after the job is done")
      context.system.shutdown()
    }
  }
}

class UnstableException extends Exception

object Main3 {
  def main(args: Array[String]) {

    val system = ActorSystem("main3-actor-system")

    val actor = system.actorOf(Props[UnstableActor], "unstable-actor")
    for (i <- (0 until 5)) {
      actor ! Greeter.Greet
    }
    actor ! Greeter.Done

  }
}

