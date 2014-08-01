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
    var total = 0
    for (i <- 0 until digits.length) {
      if (i % 2 != 0) {
        total += digits(i)
      } else {
        val doubleDigit = digits(i) << 1
        total = total + (if (doubleDigit > 9) doubleDigit / 10 + doubleDigit % 10 else doubleDigit)
      }
    }

    if (total % 10 != 0) return false
    true
  }
}

object CreditCardValidator {
  def main(args: Array[String]) {
    val validator = new CreditCardValidator
    println( s"""validator.validate("4417123456789113")=${validator.validate("4417123456789113")}""")
    println( s"""validate(ssssssssssssssss)=${validator.validate("ssssssssssssssss")}""")
    println( s"""validate(sssssssssssssss)=${validator.validate("sssssssssssssss")}""")
  }
}