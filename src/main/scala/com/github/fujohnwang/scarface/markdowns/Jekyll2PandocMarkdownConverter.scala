package com.github.fujohnwang.scarface.markdowns


import java.io._
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils

object Jekyll2PandocMarkdownConverter {
  val DEFAULT_ENCODING = "utf8"

  def main(args: Array[String]) {
    val sourceDirectory = new File("/Users/fujohnwang/Public/Dropbox/_my_markdowns")
    val destinationDir = new File(sourceDirectory, "output")
    if (!destinationDir.exists()) destinationDir.mkdirs()
    destinationDir.listFiles().foreach(_.delete)

    sourceDirectory.listFiles(new FileFilter {
      def accept(f: File) = f.isFile && (!f.isHidden)
    }).foreach(f => {
      val date = f.getName.substring(0, 10)
      val lines = scala.io.Source.fromFile(f, DEFAULT_ENCODING).getLines().toArray
      val title: String = StringUtils.substringAfter(lines(2), "title:").trim
      val filename = StringUtils.substringAfter(lines(3), "permalink:").trim
      val destinationFile: File = new File(destinationDir, f.getName)

      FileUtils.write(destinationFile, s"% $title\r\n", DEFAULT_ENCODING, true)
      FileUtils.write(destinationFile, "% fujohnwang\r\n", DEFAULT_ENCODING, true)
      FileUtils.write(destinationFile, s"% $date\r\n", DEFAULT_ENCODING, true)

      lines.slice(5, lines.length).foreach(line=>{
        if (line.startsWith("#")){
          val l = line.substring(1)
          FileUtils.write(destinationFile, s"$l\r\n", DEFAULT_ENCODING, true)
        } else{
          FileUtils.write(destinationFile, s"$line\r\n", DEFAULT_ENCODING, true)
        }
      })
    })
  }
}