package com.github.fujohnwang.scarface.http

import com.ning.http.client._

object NingHttpClientDemo {
  def main(args: Array[String]) {
    val client = new AsyncHttpClient
    try {
      val bodyFuture = client.prepareGet("http://www.baidu.com/s?wd=test").execute(new AsyncCompletionHandler[String] {
        def onCompleted(response: Response) = response.getResponseBody

        override def onThrowable(t: Throwable) {
          println(s"exception:$t")
        }
      })
      println(bodyFuture.get()) // not good since will block the thread
    } finally {
      client.close()
    }
  }
}