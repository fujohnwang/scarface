package com.github.fujohnwang.scarface.markdowns

import java.io._
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils

object PandocMarkdownTransfomer {
  def main(args: Array[String]) {
    import sys.process._

    val logger = ProcessLogger(
      (o: String) => println("out " + o),
      (e: String) => println("err " + e))

    val sourceDir = new File("/Users/fujohnwang/Public/Dropbox/_my_markdowns/output")
    val destinationDir = new File("/Users/fujohnwang/Public/Dropbox/_my_markdowns/html")
    if (!destinationDir.exists()) destinationDir.mkdirs()
    destinationDir.listFiles().foreach(_.delete())

    val stylesheetLocation = "/Users/fujohnwang/workspace/pandocakyll/stylesheets/bootstrap.min.css"
    val templateLocation = "/Users/fujohnwang/workspace/pandocakyll/template"
    sourceDir.listFiles().foreach(f => {
      val sourceFile = f.getAbsolutePath
      val baseName = StringUtils.substringBeforeLast(f.getName, ".")
      val targetFileName = s"$destinationDir/$baseName.html"
      val targetFile = new File(targetFileName)

      val command = s"pandoc -s -N --toc -c $stylesheetLocation --template=$templateLocation -f markdown -t html $sourceFile"

      FileUtils.write(targetFile, command !!, "utf8")
    })
  }
}