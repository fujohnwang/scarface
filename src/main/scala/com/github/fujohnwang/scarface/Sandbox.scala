package com.github.fujohnwang.scarface

import com.typesafe.config.ConfigFactory
import flex.messaging.io.amf.client.AMFConnection
import java.security.MessageDigest
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object Scarface extends Author {
  def main(args: Array[String]) {
//
//    val config = ConfigFactory.load()
//    println(config.getString("sample_config"))


    val conn = new AMFConnection
    try {
      conn.connect("http://index.baidu.com/gateway.php")
      conn.addHttpRequestHeader("Origin","http://index.baidu.com")
      conn.addHttpRequestHeader("Content-Type", "application/x-amf")
      conn.addHttpRequestHeader("Referer", "http://index.baidu.com/fla/TrendAnalyserbc10e6ba.swf")
      conn.addHttpRequestHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.93 Safari/537.36")


      val result = conn.call("DataAccessor.getIndexes", "女装品牌", "0", "", "13e865257336953b25", "19ab4ddf0cb22b4510e799d3de076f3c")
      println(result.getClass)
      println(result)


    } finally {
      conn.close()
    }







    //
    //    val encodedUrl = java.net.URLEncoder.encode("http://afoo.me/抗震设计.html", "utf-8")
    //    println(s"encoded url = $encodedUrl")
    //
    //    val decodedUrl = java.net.URLDecoder.decode(encodedUrl, "utf-8")
    //    println(s"decodedUrl=$decodedUrl")
    //
    //
    //    println(java.net.URLEncoder.encode("空格 plus+", "utf-8"))
    //
    //    val uri = new URI("http", "afoo.me", "/抗震 +设计.html", null, null)
    //    val encodeduri = uri.toASCIIString
    //    println(encodeduri)  // what we want
    //    println(uri.toString)                // just for display,not for our use
    //    println(URI.create(encodeduri).getRawPath) // raw, not decoded
    //    println(URI.create(encodeduri).getPath) // decoded

  }
}


trait Author {
  val aliases = Set("王福强", "千任", "@囚千任")
  val bu = "TMall"
  val group = "Alibaba"
}






