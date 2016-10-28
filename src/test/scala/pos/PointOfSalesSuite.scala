package pos

import org.scalatest.{Matchers, WordSpec}
import TestData._
import pos.PointOfSales.{BulkPrice, PriceList}

import scala.collection.SortedSet
import scala.util.{Failure, Success}

class PointOfSalesSuite extends WordSpec with Matchers {

  "A basket with ABCDABA " should {
    "have total price 13.25" in {
      val terminal = new PointOfSales()
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
      val terminal = new PointOfSales()
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
      val terminal = new PointOfSales()
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
      val terminal = new PointOfSales()
      terminal.setPricing(catalogue)
      terminal.calculateTotal() shouldEqual Success(0.0)
    }
  }
  "An pricelist with multiple BulkPrices  " should {
    "have total price 6.85" in {
      val terminal = new PointOfSales()
      terminal.setPricing(catalogueZ)
      terminal.scan("Z")
      terminal.scan("Z")
      terminal.scan("Z")
      terminal.scan("Z")
      terminal.scan("Z")
      terminal.scan("Z")
      terminal.scan("Z")
      terminal.calculateTotal() shouldEqual Success(6.85)
    }
  }
  "A BulkPrice with unit quantity value " should {
    "give Quantities in a Price must be larger than 1 error" in {
      val thrown = the[IllegalArgumentException] thrownBy BulkPrice(1, 5.00)
      thrown.getMessage should equal("requirement failed: Bulkprice quantity must be larger than 1 - found: 1")
    }
  }
  "A BulkPrice with 0 price value " should {
    "give Quantities in a Price must be larger than 1 error" in {
      val thrown = the[IllegalArgumentException] thrownBy BulkPrice(2, 0.00)
      thrown.getMessage should equal("requirement failed: Bulkprice price must be larger than 0 - found: 0.0")
    }
  }
  "A PriceList with 0 unit value " should {
    "give Unit price must be larger than 0 error" in {
      val thrown = the[IllegalArgumentException] thrownBy PriceList(0.00)
      thrown.getMessage should equal("requirement failed: Unit price must be larger than 0 - found: 0.0")
    }
  }
  "A basket with an undefined product (E) " should {
    "give key not found error" in {
      val terminal = new PointOfSales()
      terminal.setPricing(catalogue)
      terminal.scan("E")
      terminal.calculateTotal().toString shouldEqual ("Failure(java.util.NoSuchElementException: key not found: E)")
    }
  }
}
