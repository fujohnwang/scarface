package com.github.fujohnwang.scarface.akka.selection

import java.util.concurrent.TimeUnit

import akka.actor._


class HelloActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg@_ => this.log.info(s"hello :  $msg.")
  }
}

class DeadLetterHandler extends Actor with ActorLogging {
  override def receive: Actor.Receive = {
    case d: DeadLetter => log.info(s"received dead letter message: ${d.message}")
  }
}

/**
 * 使用起来不爽的地方：
 * 1. 傻逼命名限制， 只能字符数字加横岗；（当然，也不是不可忍受，只是需要记住）
 * 2. selection如果不按照路径指定查找参数，比如我要找hello-actor， 并且直接传入这个名字，会查找不到， 而且ActorSelection实例居然没有方法可以判断选取了多少符合条件的actors;
 */
object ActorSelectionDemo {
  def main(args: Array[String]) {
    val world = ActorSystem("actor-world")
    val helloActor = world.actorOf(Props[HelloActor], "hello-actor")

    helloActor ! "hello"

    val selection = world.actorSelection("/user/hello-actor")
    selection ! "world"


    // best practice
    world.actorSelection(helloActor.path) ! "safe"

    // bad case
    val deadLetterListener = world.actorOf(Props[DeadLetterHandler])
    world.eventStream.subscribe(deadLetterListener, classOf[DeadLetter])
    world.actorSelection("hello-actor") ! "haha, missing"

    TimeUnit.SECONDS.sleep(1)

    world.shutdown()
  }
}