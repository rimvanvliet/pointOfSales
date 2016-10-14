package pos

/**
  * Created by ruud on 12/10/2016.
  */
object TestData {

  val priceListA = List((1, 1.25), (3, 3.0))
  val priceListB = List((1, 4.25))
  val priceListC = List((1, 1.0), (6, 5.0))
  val priceListD = List((1, 0.75))
  val priceList2 = List((2, 0.75))
  val priceList0 = List((0, 0.75))

  val catalogue = Map(
    "A" -> priceListA,
    "B" -> priceListB,
    "C" -> priceListC,
    "D" -> priceListD)

  val catalogue2 = Map(
    "A" -> priceList2)

  val catalogue0 = Map(
    "A" -> priceList0)
}
