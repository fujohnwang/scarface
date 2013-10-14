package com.github.fujohnwang.scarface.zxing

import javax.imageio.ImageIO
import java.io.File
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.{DecodeHintType, BinaryBitmap}
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import java.util


/**
 * damn, the 3rd is not detected properly!
 *
 * https://groups.google.com/forum/#!topic/zxing/vzSIkblQYig
 *
 */
object QRCodeDetector {
  def main(args: Array[String]) {
    //    val image = ImageIO.read(new File("qrcodesample.jpg"))

    val hints = new util.HashMap[DecodeHintType, Any]()
    hints.put(DecodeHintType.TRY_HARDER, true)


    val imageFiles = Array(new File("T2Da0ZXbVeXXXXXXXX_!!294627560.jpg"),new File("/Users/fujohnwang/workspace/scarface/qrcodesample.jpg"), new File("/Users/fujohnwang/workspace/scarface/T1dmmqFXtdXXcze_bB-790-205.jpg"))
    imageFiles.foreach(file => {
      val image = ImageIO.read(file)
      if (image == null) throw new Exception("Could not decode image")
      //      image = Scalr.resize(image, 150)
      val bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)))
      try {
        //                val result = new MultiFormatReader().decode(bitmap)
        val results = new QRCodeMultiReader().decodeMultiple(bitmap, hints)
        if (results != null) {
          println(results.foldLeft("")((sum, result) => sum + result.getText))
        }
      } catch {
        case ex: Throwable => ex.printStackTrace()
      }

    })
  }
}