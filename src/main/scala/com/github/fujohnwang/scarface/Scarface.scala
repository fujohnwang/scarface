package com.github.fujohnwang.scarface

import com.typesafe.config.ConfigFactory

object Scarface {
  def main(args: Array[String]) {

    val config = ConfigFactory.load()
    println(config.getString("sample_config"))


  }
}







