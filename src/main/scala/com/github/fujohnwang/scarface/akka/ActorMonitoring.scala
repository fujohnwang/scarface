package com.github.fujohnwang.scarface.akka

import akka.actor._
import akka.actor.Terminated

class WatchedActor extends Actor with ActorLogging {
  def receive = {
    case msg: String => log.info("message recieves: {}", msg)
    case _ => log.warning("unexpected message type")
  }
}

class ActorWatcher(watchedActor: ActorRef) extends Actor with ActorLogging {

  context.watch(watchedActor)

  def receive = {
    case Terminated(actor) => {
      if (actor == watchedActor) {
        log.info("watched actor is terminated, unwatch it.")
        context.unwatch(watchedActor)

        log.info("shutdown the actor system at the same time, there will be no more processing")
        context.system.shutdown()
      }
    }
  }
}

/**
 * refer to <a href="http://blog.evilmonkeylabs.com/2013/01/17/Distributing_Akka_Workloads_And_Shutting_Down_After/">http://blog.evilmonkeylabs.com/2013/01/17/Distributing_Akka_Workloads_And_Shutting_Down_After/</a>
 *
 * a great post on this issue!
 */
object ActorMonitoring {
  def main(args: Array[String]) {
    val actorSystem = ActorSystem("test-system")

    val actor = actorSystem.actorOf(Props[WatchedActor], "watchedActor")
    actorSystem.actorOf(Props(new ActorWatcher(actor)), "watcher")

    actor ! "test message"
    actor ! PoisonPill
  }
}