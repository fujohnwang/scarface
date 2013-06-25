package com.github.fujohnwang.scarface.http

import com.ning.http.client._
import java.util.concurrent.TimeUnit

object NingHttpClientDemo {
  def main(args: Array[String]) {
    val client = new AsyncHttpClient
    try {
      //      val bodyFuture = client.prepareGet("http://www.baidu.com/s?wd=test").execute(new AsyncCompletionHandler[String] {
      //        def onCompleted(response: Response) = response.getResponseBody
      //
      //        override def onThrowable(t: Throwable) {
      //          println(s"exception:$t")
      //        }
      //      })
      //      println(bodyFuture.get()) // not good since will block the thread


      val response = client.preparePost("http://index.baidu.com/gateway.php")
        .addHeader("Origin", "http://index.baidu.com")
        .setHeader("Content-Type", "application/x-amf")
        .setHeader("Referer", "http://index.baidu.com/fla/TrendAnalyserbc10e6ba.swf")
        .setHeader("Accept-Encoding", "gzip,deflate,sdch")
        .setHeader("Accept-Language", "en-US,en;q=0.8")
        .setBody("\u0003\u0001\u0017DataAccessor.getIndexes\u0002/1S").execute().get

      println(response.getStatusCode)
      println(response.getHeaders)
      println(response.getResponseBody)

    } finally {
      client.close()
    }
  }
}