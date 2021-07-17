package dev.adamko.gildedrose.testdata

import com.gildedrose.Item
import dev.adamko.config.KotestConfig.Companion.intEdgecases
import dev.adamko.config.KotestConfig.Companion.mergeAll
import io.kotest.property.Arb
import io.kotest.property.Gen
import io.kotest.property.arbitrary.alphanumeric
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.withEdgecases
import io.kotest.property.exhaustive.exhaustive

object ItemGens {

  object Names {
    val regular = Arb.string(10, Arb.alphanumeric()).withEdgecases(
        "+5 Dexterity Vest",
        "Elixir of the Mongoose",
    )
    val aged = listOf("Aged Brie").exhaustive()
    val tickets = listOf("Backstage passes to a TAFKAL80ETC concert").exhaustive()
    val legendary = listOf("Sulfuras, Hand of Ragnaros").exhaustive()

    val conjured = listOf(regular, aged, tickets, legendary).mergeAll().map { "Conjured $it" }

    val all = listOf(regular, aged, tickets, conjured, legendary).mergeAll()
    val nonLegendary = listOf(regular, aged, tickets, conjured).mergeAll()
  }

  object SellIn {

    /**
     * Edgecases for the minimum and maximum values, as well as some that
     * are centered around the expiration date of 0.
     */
    private val edgecases: List<Int> = (-2..2) + (Int.MAX_VALUE - 1) + (Int.MIN_VALUE + 1)

    // note- ignore integer overflow for now
    val nonExpired = Arb.intEdgecases(1, Int.MAX_VALUE - 1, edgecases)
    val expired = Arb.intEdgecases(Int.MIN_VALUE + 1, 0, edgecases)
    val any = Arb.intEdgecases(Int.MIN_VALUE + 1 until Int.MAX_VALUE, edgecases)
  }

  object Quality {
    val regularValidRange = 0..50

    /** Some additional edgecases around the min/max quality range */
    private val edgecases: List<Int> =
        regularValidRange.first.let { (it - 3)..(it + 3) } +
        regularValidRange.last.let { (it - 3)..(it + 3) }

    val any = Arb.intEdgecases(Int.MIN_VALUE + 1 until Int.MAX_VALUE, edgecases)

    val validRegular = Arb.intEdgecases(regularValidRange, edgecases)

    // note- ignore integer overflow for now
    val invalidRegular = any.filterNot { it in regularValidRange }
  }

  object Binds {
    fun itemArb(
        nameGen: Gen<String> = Names.all,
        sellInGen: Gen<Int> = SellIn.any,
        qualityGen: Gen<Int> = Quality.any,
    ) = Arb.bind(nameGen, sellInGen, qualityGen) { name, sellIn, quality ->
      Item(name, sellIn, quality)
    }
  }

}
