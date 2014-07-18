package myactor

import java.util.concurrent.ConcurrentHashMap

import scala.beans.BeanProperty

trait Actor extends Runnable {

  def takeNextMessage(): Option[Any]

  def onReceive(message: Any): Unit

  override def run(): Unit = {
    takeNextMessage() match {
      case None => return
      case Some(message) => onReceive(message)
    }

  }
}


trait ActorRegistry {
  def find(name: String): Option[Actor]

  def register(name: String, actor: Actor): Unit
}


class SimpleLocalActorRegistry extends ActorRegistry {
  var actorRegistry = new ConcurrentHashMap[String, Actor]()

  override def find(name: String): Option[Actor] = {
    actorRegistry.get(name) match {
      case null => None
      case actor@_ => Some(actor)
    }
  }

  def register(name: String, actor: Actor) {
    actorRegistry.put(name, actor)
  }
}

class ActorWorld extends ActorRegistry {

  @BeanProperty
  var actorRegistry: ActorRegistry = new SimpleLocalActorRegistry

  override def find(name: String): Option[Actor] = actorRegistry.find(name)

  override def register(name: String, actor: Actor): Unit = actorRegistry.register(name, actor)
}