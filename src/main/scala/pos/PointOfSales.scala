package pos

object PointOfSales {

  type Product = String // just the name of the product

  type Price = (Int, Double) // (quantity, price), the price for a given quantity, to allow discounts for larger quantities

  type PriceList = List[Price]

  type Catalogue = Map[Product, PriceList] // the PriceList for each product

  type Basket = Map[Product, Int] // the scanned quantity per Product

  def addProduct2Basket(product: Product, basket: Basket): Basket = {
    basket.get(product) match {
      case Some(quantity) => basket + (product -> (quantity + 1))
      case None => basket + (product -> 1)
    }
  }

  private def calculateArticlePrice(quantity: Int, priceList: PriceList): Double = {
    def accCalc(quantity: Int, priceList: PriceList, acc: Double): Double = priceList match {
      case Nil => acc
      case (qty, prc) :: rest => accCalc(quantity % qty, rest, acc + prc * (quantity / qty))
    }
    accCalc(quantity, priceList.sorted.reverse, 0.0)
  }

  private def calculateBasketPrice(basket: Basket, catalogue: Catalogue): Double = {
    val articlePrices: Iterable[Double] = for {
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

  def calculateTotal(): Double = calculateBasketPrice(basket, catalogue)
}
