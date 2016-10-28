package pos

import pos.PointOfSales.{BulkPrice, PriceList}

import scala.collection.immutable.SortedSet

/**
  * Created by ruud on 12/10/2016.
  */
object TestData {

  val priceListA = PriceList(1.25, SortedSet(new BulkPrice(3, 3.00)))
  val priceListB = PriceList(4.25)
  val priceListC = PriceList(1.0, SortedSet(new BulkPrice(6, 5.00)))
  val priceListD = PriceList(0.75)

  val catalogue = Map(
    "A" -> priceListA,
    "B" -> priceListB,
    "C" -> priceListC,
    "D" -> priceListD)

  val priceListZ: PriceList  = PriceList(1.25, SortedSet(BulkPrice(2, 2.0), BulkPrice(4, 3.6)))

  val catalogueZ = Map(
    "Z" -> priceListZ)
}