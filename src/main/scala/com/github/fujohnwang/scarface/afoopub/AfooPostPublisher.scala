package com.github.fujohnwang.scarface.afoopub

import scala.util.Try
import com.typesafe.config.ConfigFactory
import java.io.File
import org.slf4j.helpers.MessageFormatter
import org.eclipse.jgit.api.Git
import sys.process._
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.IOUtils
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory
import scala.util.Failure
import scala.util.Success
import org.eclipse.jgit.transport.{URIish, CredentialItem, CredentialsProvider}


class AfooPostPublisher {

  package
}


class PassphraseCredentialsProvider(passphrase: String) extends CredentialsProvider {
  def isInteractive: Boolean = true

  def supports(items: CredentialItem*): Boolean = true

  def get(uri: URIish, items: CredentialItem*): Boolean = {
    setPassphrase(items: _*)
    true
  }

  protected def setPassphrase(items: CredentialItem*) {
    for (item <- items) {
      if (item.isInstanceOf[CredentialItem.StringType]) {
        item.asInstanceOf[CredentialItem.StringType].setValue("Zero1one")
        return
      }
    }
  }
}

object AfooPostPublisher {
  def main(args: Array[String]) {
    val publisher = new AfooPostPublisher
    publisher.publish("2013-06-11-how_to_access_private_ip_from_internet.md") match {
      case Success(message) => println(message)
      case Failure(t) => println(t)
    }
  }
}