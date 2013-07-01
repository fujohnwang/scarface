package com.github.fujohnwang.scarface

import java.net.URI


object Scarface extends Author {
  def main(args: Array[String]) {
    //
    //    val config = ConfigFactory.load()
    //    println(config.getString("sample_config"))


    val encodedUrl = java.net.URLEncoder.encode("http://afoo.me/女装 精选.html", "utf-8")
    println(s"encoded url = $encodedUrl")

    val decodedUrl = java.net.URLDecoder.decode(encodedUrl, "utf-8")
    println(s"decodedUrl=$decodedUrl")


//    println(java.net.URLEncoder.encode("空格 plus+", "utf-8"))

    val uri = new URI("http", "afoo.me", "/女装 精选.html", null, null)
    val encodeduri = uri.toASCIIString
    println(encodeduri) // what we want
    println(uri.toString) // just for display,not for our use
    println(URI.create(encodeduri).getRawPath) // raw, not decoded
    println(URI.create(encodeduri).getPath) // decoded


    val url = "http://afoo.me/女装 精选.html"
    println(new String(url.getBytes("utf8"), "gb2312"))
  }
}


trait Author {
  val aliases = Set("王福强", "千任", "@囚千任")
  val bu = "TMall"
  val group = "Alibaba"
}






