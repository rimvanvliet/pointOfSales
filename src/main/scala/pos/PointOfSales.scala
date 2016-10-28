package pos

import scala.annotation.tailrec
import scala.collection.SortedSet
import scala.util.{Failure, Success, Try}

object PointOfSales {

  case class BulkPrice(quantity: Int, price: BigDecimal) extends Ordered[BulkPrice] {
    require(quantity > 1, s"Bulkprice quantity must be larger than 1 - found: $quantity")
    require(price > 0, s"Bulkprice price must be larger than 0 - found: $price")

    def compare(that: BulkPrice) = that.quantity - quantity // reversed order, so largest first!
  }

  case class PriceList(unitPrice: BigDecimal, bulkPrices: SortedSet[BulkPrice] = SortedSet.empty) {
    require(unitPrice > 0, s"Unit price must be larger than 0 - found: $unitPrice")
  }

  type Product = String // just the name of the product

  type Catalogue = Map[Product, PriceList] // the PriceList for each product

  type Basket = Map[Product, Int] // the scanned quantity per Product

  def addProduct2Basket(product: Product, basket: Basket): Basket =
    basket + (product -> (basket.getOrElse(product, 0) + 1))

  def calculateArticlePrice(quantity: Int, priceList: PriceList): BigDecimal = {
    @tailrec
    def accCalc(quantity: Int, bulkPrices: Seq[BulkPrice], acc: BigDecimal): BigDecimal =
      bulkPrices match {
        case _ if bulkPrices.isEmpty => acc + quantity * priceList.unitPrice
        case BulkPrice(qty, prc) +: rest => accCalc(quantity % qty, rest, acc + prc * (quantity / qty))
      }
    accCalc(quantity, priceList.bulkPrices.toSeq, 0.0)
  }

  private def calculateBasketPrice(basket: Basket, catalogue: Catalogue): BigDecimal = {
    val articlePrices = for {
      (product, quantity) <- basket
      articlePrice = calculateArticlePrice(quantity, catalogue(product))
    } yield articlePrice
    articlePrices.sum
  }
}

class PointOfSales() {

  import pos.PointOfSales._

  private var catalogue: Catalogue = Map.empty

  def setPricing(catalogue: Catalogue): Unit = this.catalogue = catalogue

  private var basket: Basket = Map.empty

  def scan(product: Product): Unit = basket = addProduct2Basket(product, basket)

  def calculateTotal(): Try[BigDecimal] = Try(calculateBasketPrice(basket, catalogue))
}