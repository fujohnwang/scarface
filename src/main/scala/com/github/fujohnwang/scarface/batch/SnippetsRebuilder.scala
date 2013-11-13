package com.github.fujohnwang.scarface.batch

import java.io.File
import scala.xml.{Node, XML}
import org.apache.commons.io.FileUtils
import java.nio.charset.Charset
import org.slf4j.LoggerFactory
import org.apache.commons.lang3.StringEscapeUtils

/**
 * sbt "run-main com.github.fujohnwang.scarface.batch.SnippetsRebuilder"
 */
object SnippetsRebuilder {
  val logger = LoggerFactory.getLogger("SnippetsRebuilder")
  // sed 's/\\u\([a-z0-9]\{2\}\)\([a-z0-0]\{2\}\)/\\u\2\1/g' /Users/fujohnwang/Documents/Snippets.xml
  val sourceFile = new File("/Users/fujohnwang/Documents/pre-processed-snippets.xml")

  val targetFolder = new File("snippets")
  val encoding = Charset.forName("utf8")

  def main(args: Array[String]) {
    FileUtils.deleteDirectory(targetFolder)
    FileUtils.forceMkdir(targetFolder)

    val doc = XML.loadFile(sourceFile)

    val nodes = (((doc \\ "object").find(node => (node \ "@id").text.equals("z381")).get \ "relationship" last) \ "@idrefs" text).split(" ")
    process(nodes, doc, targetFolder)
  }


  def process(nodes: Array[String], doc: Node, folder: File): Unit = {
    logger.info(s"process ${nodes.mkString(" ")} under folder: ${folder.getAbsolutePath}")

    nodes.foreach(id => {
      (doc \\ "object").find(n => (n \ "@id").text == id).foreach(node => {

        logger.info(s"process node with id=${id}")

        (node \ "@type").text match {
          case "FOLDER" => {
            val subFolder = new File(folder, (node \ "attribute").find(n => (n \ "@name").text == "name").get.text)
            logger.info(s"create folder:$subFolder")
            FileUtils.forceMkdir(subFolder)

            val childrenIdList = ((node \ "relationship").find(n => (n \ "@name").text == "children").get \ "@idrefs" text).split(" ")
            process(childrenIdList, doc, subFolder)
          }
          case "GROUP" => {
            val subFolder = new File(folder, (node \ "attribute").find(n => (n \ "@name").text == "name").get.text)
            logger.info(s"create folder:$subFolder")
            FileUtils.forceMkdir(subFolder)

            val childrenIds = ((node \ "relationship").find(n => (n \ "@name").text == "snippets").get \ "@idrefs" text).split(" ")
            process(childrenIds, doc, subFolder)
          }
          case "SNIPPET" => {
            val name = (node \ "attribute").find(n => (n \ "@name").text == "name").get.text.replace("/", "-")
            val id = ((node \ "relationship").find(n => (n \ "@name").text == "code").get \ "@idrefs").text
            (doc \\ "object").find(n => (n \ "@id").text == id).foreach(n => {
//              val content = StringEscapeUtils.unescapeXml((n \ "attribute").text)

              val content = StringEscapeUtils.unescapeJava((n \ "attribute").text)
              logger.info(s"write content to file :$folder - $name - $content")
              FileUtils.writeStringToFile(new File(folder, name + ".markdown"), content, encoding)
            })
          }
          case _ => logger.info("ignore other node")
        }
      })
    })
  }
}