package pos

import org.scalatest.{ Matchers, WordSpec }
import TestData._

class PointOfSalesSuite extends WordSpec with Matchers {

  "A basket with ABCDABA " should {
    "have total price 13.25" in {
      val terminal = new PointOfSales
      terminal.setPricing(catalogue)
      terminal.scan("A")
      terminal.scan("B")
      terminal.scan("C")
      terminal.scan("D")
      terminal.scan("A")
      terminal.scan("B")
      terminal.scan("A")
      terminal.calculateTotal() shouldEqual 13.25
    }
  }
  "A basket with CCCCCCC " should {
    "have total price 6.0" in {
      val terminal = new PointOfSales
      terminal.setPricing(catalogue)
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.scan("C")
      terminal.calculateTotal() shouldEqual 6.0
    }
  }
  "A basket with ABCD " should {
    "have total price 7.25" in {
      val terminal = new PointOfSales
      terminal.setPricing(catalogue)
      terminal.scan("A")
      terminal.scan("B")
      terminal.scan("C")
      terminal.scan("D")
      terminal.calculateTotal() shouldEqual 7.25
    }
  }
}
