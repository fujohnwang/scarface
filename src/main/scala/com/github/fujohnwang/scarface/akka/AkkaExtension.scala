package com.github.fujohnwang.scarface.akka

import akka.actor._
import com.typesafe.config.Config

/**
 * an extension is a singleton service that can be used by actors and other part of the system.
 * It is provided by factory of ExtensionId which identify the extension. We use companion object as the factory below.
 *
 * @param config, just for configuration information to use, not mandatory constructor parameter
 */
class MyExtension(config: Config) extends Extension {
  println("initialize extension")
  // it will be singleton-scoped, so only execute once.
  val name = config.getString("akka.extension.mine.name")
}

/**
 * should be same id so that the system will not load the extension multiple times.
 */
object MyExtensionId extends ExtensionId[MyExtension] {
  def createExtension(system: ExtendedActorSystem) = new MyExtension(system.settings.config)
}


/**
 * we can think ExtensionIdProvider as FactoryBean of spring framework, and Extension as the real service type/instance.
 */
object AkkaExtension {
  def main(args: Array[String]) {
    val actorSystem = ActorSystem("akka-extension")


    val extension = actorSystem.registerExtension(MyExtensionId)

    //    val extension = MyExtensionId.get(actorSystem) // same to MyExtensionId(actorSystem)
    println(extension.name)



    class ExtensionUser extends Actor with ActorLogging {
      //      val extension = MyExtensionId(context.system)

      val extension = context.system.extension(MyExtensionId)

      def receive = {
        case _ => {
          log.info("message receives: {}", extension.name)
          context.system.shutdown()
        }
      }
    }



    val actor = actorSystem.actorOf(Props[ExtensionUser], "ext-user")
    actor ! "test message"

  }
}