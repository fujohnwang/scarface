import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import java.io._

object YAMLFrontHeaderAdder {
  def main(args: Array[String]) {

    val sourceDir = new File("/Users/fujohnwang/workspace/fujohnwang.github.com/_posts")
    sourceDir.listFiles().foreach(f => FileUtils.moveFile(f, new File(sourceDir, f.getName + ".bak")))
    sourceDir.listFiles(new FileFilter {
      def accept(f: File) = f.isFile && (!f.isHidden)
    }).foreach(f => {
      val content = FileUtils.readFileToString(f, "utf8")
      val title = StringUtils.substringBetween(content, "<title>", "</title>")
      val permalink = StringUtils.substring(StringUtils.substringBeforeLast(f.getName, "."), 11)
      val yamlFrontHeader = s"""---\r\nlayout: default\r\ntitle: $title\r\npermalink: $permalink\r\n---\r\n\r\n"""
      val targetFile = new File(sourceDir, StringUtils.substringBeforeLast(f.getName, "."))

      println(yamlFrontHeader)
      println(targetFile)
      FileUtils.write(targetFile, yamlFrontHeader, "utf8", true)
      FileUtils.write(targetFile, content, "utf8", true)
    })
  }
}