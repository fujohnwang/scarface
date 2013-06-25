package com.github.fujohnwang.scarface.process

import sys.process._
import java.io.File
import scala.util.{Failure, Success, Try}
import org.slf4j.helpers.MessageFormatter

object Pandoc {

  val basicCommand = "pandoc -s -N --toc {} -o {}"
  val fullCommand = basicCommand + " -c {} --template={}"

  def compile(input: File, output: File): Try[Int] = compile(input.getAbsolutePath, output.getAbsolutePath)

  def compile(input: String, output: String): Try[Int] = {
    val (_, err, logger) = getProcessBuffers
    if ((MessageFormatter.format(basicCommand, input, output).getMessage ! logger) == 0) Success(0) else Failure(new Exception(err.toString))
  }

  def compile(input: String, output: String, style: String, templateFileLocation: String): Try[Int] = {
    val (_, err, logger) = getProcessBuffers
    if ((MessageFormatter.arrayFormat(fullCommand, Array(input, output, style, templateFileLocation)).getMessage ! logger) == 0) Success(0) else Failure(new Exception(err.toString))
  }

  private def getProcessBuffers() = {
    val (out, err) = (new StringBuilder, new StringBuilder)
    (out, err, ProcessLogger(o => out.append(o), e => err.append(e)))
  }

  def main(args: Array[String]) {
    Pandoc.compile("/Users/fujohnwang/Documents/docworks/articles/2013-06-05-install_java_on_pcduino.md", "./2013-06-05-install_java_on_pcduino.html") match {
      case Success(_) => println("compile successfully")
      case Failure(e) => println(s"failed to compile: $e")
    }

    Pandoc.compile("/Users/fujohnwang/Documents/docworks/articles/2013-06-05-install_java_on_pcduino.md", "./2013-06-05-install_java_on_pcduino.htm", "stylesheets/bootstrap.min.css", "template.html") match {
      case Success(_) => println("compile successfully")
      case Failure(e) => println(s"failed to compile: $e")
    }
  }
}

