package com.github.fujohnwang.scarface.process

object ProcessRunner {
  def main(args: Array[String]) {
    import sys.process._

    val out = new StringBuilder
    val err = new StringBuilder
    val logger = ProcessLogger(o => out.append(o), e => err.append(e))
    val input = "/Users/fujohnwang/Documents/docworks/articles/2013-06-05-install_java_on_pcduino.md"
    val output = "./2013-06-05-install_java_on_pcduino.html"
    val result = s"pandoc -s -N --toc $input -o $output" ! logger
    if (result != 0) {
      println(err)
    }
  }
}