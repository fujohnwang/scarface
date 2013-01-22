package com.github.fujohnwang.scarface

import com.typesafe.config.ConfigFactory
import java.util.Date


object Scarface {
  def main(args: Array[String]) {
    println("hello world")
    println(new Date)

    val config = ConfigFactory.load()
    println(config.getString("sample_config"))
  }
}