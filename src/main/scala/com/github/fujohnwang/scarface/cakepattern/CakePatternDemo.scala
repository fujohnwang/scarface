package com.github.fujohnwang.scarface.cakepattern

abstract class XMan {
  def iHavePower(power:String): String
}

class Afoo extends XMan{
  def iHavePower(power: String): String = power
}

trait SuperMan extends XMan{
  abstract override def iHavePower(power:String) = super.iHavePower("fly, " + power)
}

trait IronMan extends XMan{
  abstract override def iHavePower(power:String) = super.iHavePower("be handsome, "+power)
}

trait GreenGiant extends XMan{
  abstract override def iHavePower(power:String) = super.iHavePower("titan and transform, " + power)
}



object XManDemo {
  def main (args: Array[String] ) {
    val afoo = new Afoo
    val afooPower = afoo.iHavePower("an average man")
    println(s"I am afoo, I have powers of $afooPower")

    val afoo2 = new Afoo with SuperMan with IronMan with GreenGiant
    val afoo2Power = afoo2.iHavePower("an average man")
    println(s"I am afoo, I have powers of $afoo2Power")
  }
}






