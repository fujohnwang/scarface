package com.github.fujohnwang.scarface

import com.typesafe.config.ConfigFactory
import stats.StatHat;

object Scarface {
  def main(args: Array[String]) {

    val config = ConfigFactory.load()
    println(config.getString("sample_config"))

    StatHat.ezPostCount("fujohnwang@gmail.com", "weibo access count", 1.0)
  }
}







