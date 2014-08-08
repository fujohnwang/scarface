package creditcard

import org.apache.commons.lang3.StringUtils

import scala.beans.BeanProperty

/**
 * http://www.getcreditcardnumbers.com/
 * http://en.wikipedia.org/wiki/Luhn_algorithm
 */
class CreditCardValidator {
  @BeanProperty
  var cardNumberLength = 16

  def validate(cardNumber: String): Boolean = {
    val numberString = StringUtils.deleteWhitespace(cardNumber)
    if (numberString.length != cardNumberLength) return false
    if (!StringUtils.isNumeric(numberString)) return false
    val digits = numberString.map(c => java.lang.Byte.valueOf(String.valueOf(c)))
    val total = digits.zipWithIndex.foldLeft(0)((total, e) => if (e._2 % 2 != 0) total + e._1 else total + doubleFlat(e._1))
    if (total % 10 != 0) false else true
  }

  def doubleFlat(value: Byte) = {
    val doubleValue = value << 1
    if (doubleValue > 9) doubleValue / 10 + doubleValue % 10 else doubleValue
  }
}

object CreditCardValidator {
  def main(args: Array[String]) {
    val validator = new CreditCardValidator
    println( s"""validator.validate("4417123456789113")=${validator.validate("4417123456789113")}""")
    println( s"""validator.validate("4417123456789111")=${validator.validate("4417123456789111")}""")
    println( s"""validate(ssssssssssssssss)=${validator.validate("ssssssssssssssss")}""")
    println( s"""validate(sssssssssssssss)=${validator.validate("sssssssssssssss")}""")
  }
}