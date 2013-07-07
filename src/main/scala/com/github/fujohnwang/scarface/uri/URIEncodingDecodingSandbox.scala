package com.github.fujohnwang.scarface.uri

import java.net.URI

object URIEncodingDecodingSandbox {
  def main(args: Array[String]) {
    val encodedUrl = java.net.URLEncoder.encode("http://afoo.me/女装 精选.html", "utf-8")
    println(s"encoded url = $encodedUrl")

    val decodedUrl = java.net.URLDecoder.decode(encodedUrl, "utf-8")
    println(s"decodedUrl=$decodedUrl")


    println(java.net.URLEncoder.encode("空格 plus+", "utf-8"))

    val uri = new URI("http", "afoo.me", "/女装 精选.html", null, null)
    val encodedUri = uri.toASCIIString
    println(encodedUri) // what we want
    println(uri.toString) // just for display,not for our use
    println(URI.create(encodedUri).getRawPath) // raw, not decoded
    println(URI.create(encodedUri).getPath) // decoded


    val url = "http://afoo.me/女装 精选.html"
    println(new String(url.getBytes("utf8"), "gb2312"))
  }
}