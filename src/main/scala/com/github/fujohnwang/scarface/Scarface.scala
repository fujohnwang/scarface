package com.github.fujohnwang.scarface

import java.io.File
import scala.xml.XML
import org.apache.commons.io.FileUtils
import org.jdom2.input.SAXBuilder
import java.text.Normalizer
import org.apache.commons.lang3.StringEscapeUtils

/**
 * sbt "run-main com.github.fujohnwang.scarface.Scarface"
 */

object Scarface {
  def main(args: Array[String]) {
//
//    val config = ConfigFactory.load()
//    println(config.getString("sample_config"))

    val sourceFile = new File("/Users/fujohnwang/Documents/pre-processed-snippets.xml")
//    val doc = XML.loadFile(sourceFile)
//    val doc = new SAXBuilder().build(sourceFile)
//    doc.getRootElement.getch











    val doc = XML.loadString(FileUtils.readFileToString(sourceFile, "utf8"))
    (doc \\ "object").find(node=> (node \ "@id").text == "z1174").foreach(node=>{
      println(s"index of \\u003c: ${node.text.indexOf("\\u003c")}")
      println(StringEscapeUtils.unescapeJava(node.text))
//      println(Normalizer.normalize(node.text, Normalizer.Form.NFC))
//      println(Normalizer.normalize(node.text, Normalizer.Form.NFD))
//      println(Normalizer.normalize(node.text, Normalizer.Form.NFKC))
//      println(Normalizer.normalize(node.text, Normalizer.Form.NFKD))
    })
  }
}







