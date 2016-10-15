package pos

import scala.annotation.tailrec
import scala.collection.SortedSet
import scala.util.{Failure, Success, Try}

object PointOfSales {


  type Product = String // just the name of the product

  type Price = (Int, Double) // (quantity, price), the price for a given quantity, to allow discounts for larger quantities

  type PriceList = List[Price]

  type Catalogue = Map[Product, PriceList] // the PriceList for each product

  type Basket = Map[Product, Int] // the scanned quantity per Product

  def addProduct2Basket(product: Product, basket: Basket): Basket =
    basket + (product -> (basket.getOrElse(product, 0) + 1))

  private def calculateArticlePrice(quantity: Int, priceList: PriceList): Try[Double] = {
    // Onderstaande lijkt op een fold.
    // Als je recursie gebruikt, is quantity==0 ook geen endstate om niet verder de recursie in te gaan?
    @tailrec //<- geeft een check dat je wel tailrecursie gebruikt en geen stackoverflow kunt krijgen.
    def accCalc(quantity: Int, priceList: PriceList, acc: Double): Double = priceList match {
      case Nil => acc
      case (qty, prc) :: rest => accCalc(quantity % qty, rest, acc + prc * (quantity / qty))
    }

    // Er wordt hieronder 3x gesorteerd. Deze check zou ik ook liever in de datastructuur zien. Zoals je kunt zien
    // kan PriceList2 niet bestaan zonder unitPrice en is de bulkPrice gesorteerd (sortering is nog niet geimplementeerd)
    case class BulkPrice(quantity: Int, price: BigDecimal) {
      require(quantity > 1)
    }
    case class PriceList2(unitPrice: BigDecimal, bulkPrices: SortedSet[BulkPrice])

    if (priceList.sorted.head._1 < 1)
      Failure(new IllegalArgumentException(s"Quantities in a Price must be >= 1 - found: ${priceList.sorted.head._1}"))
    else if (priceList.sorted.head._1 > 1)
      Failure(new IllegalArgumentException(s"Pricelist requires a unit price - found: ${priceList.sorted.head._1}"))
    else {
      // Dit is een smell. De accCalc levert een Try op, waarom dan weer wrappen in een try. Zie ook de test.
      Try(accCalc(quantity, priceList.sorted.reverse, 0.0))
    }
  }

  // Wat is de bedoeling van deze functie? Wil je alleen de 1e fout terugkrijgen, of alle fouten bij elkaar? Nu
  // Krijg je alleen de 1e fout
  private def calculateBasketPrice(basket: Basket, catalogue: Catalogue): Try[Double] = {
    basket.foldLeft(Try(0.0)) { case (acc, (product, quantity)) =>
      for {
        a <- acc
        add <- calculateArticlePrice(quantity, catalogue(product))
      } yield a + add
    }
  }
}

class PointOfSales() {

  import pos.PointOfSales._

  private var catalogue: Catalogue = Map.empty

  def setPricing(catalogue: Catalogue): Unit = this.catalogue = catalogue

  private var basket: Basket = Map.empty

  def scan(product: Product): Unit = basket = addProduct2Basket(product, basket)

  def calculateTotal(): Try[Double] = calculateBasketPrice(basket, catalogue)
}
