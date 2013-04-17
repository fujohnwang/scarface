package com.github.fujohnwang.scarface

import com.typesafe.config.ConfigFactory

object Scarface extends Author {
  def main(args: Array[String]) {

    val config = ConfigFactory.load()
    println(config.getString("sample_config"))


  }
}

trait Author {
  val aliases = Set("王福强", "千任", "达伦|Darren")
  val bu = "TMall"
  val group = "Alibaba"
}