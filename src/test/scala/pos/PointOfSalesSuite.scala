package pos

import org.scalatest.{Matchers, WordSpec}
import TestData._

import scala.util.{Failure, Success}

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
      terminal.calculateTotal() shouldEqual Success(13.25)
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
      terminal.calculateTotal() shouldEqual Success(6.0)
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
      terminal.calculateTotal() shouldEqual Success(7.25)
    }
  }
  "An empty basket  " should {
    "have total price 0.00" in {
      val terminal = new PointOfSales
      terminal.setPricing(catalogue)
      terminal.calculateTotal() shouldEqual Success(0.0)
    }
  }
  "A catalogue without unit value  " should {
    "give Price requires a unit price error" in {
      val terminal = new PointOfSales
      terminal.setPricing(catalogue2)
      terminal.scan("A")
      val thrown = the [IllegalArgumentException] thrownBy terminal.calculateTotal()
      thrown.getMessage should equal ("Pricelist requires a unit price - found: 2")
    }
  }
  "An catalogue with zero quantity value " should {
    "give Quantities in a Price must be >= 1 error" in {
      val terminal = new PointOfSales
      terminal.setPricing(catalogue0)
      terminal.scan("A")
      val thrown = the [IllegalArgumentException] thrownBy terminal.calculateTotal()
      thrown.getMessage should equal ("Quantities in a Price must be >= 1 - found: 0")
    }
  }
  "A basket with E " should {
    "give key not found error" in {
      val terminal = new PointOfSales
      terminal.setPricing(catalogue)
      terminal.scan("E")
      val thrown = the [NoSuchElementException] thrownBy terminal.calculateTotal()
      thrown.getMessage should equal ("key not found: E")
    }
  }
}
