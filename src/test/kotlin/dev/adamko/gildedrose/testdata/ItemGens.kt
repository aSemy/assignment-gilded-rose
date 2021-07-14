package dev.adamko.gildedrose.testdata

import com.gildedrose.Item
import dev.adamko.config.KotestExtensions.mergeAll
import io.kotest.property.Arb
import io.kotest.property.Gen
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.withEdgecases
import io.kotest.property.exhaustive.exhaustive

object ItemGens {

  object Names {
    val regular = Arb.string().withEdgecases(
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
    // note- ignore integer overflow for now
    val nonExpired = Arb.int(1, Int.MAX_VALUE - 1)
    val expired = Arb.int(Int.MIN_VALUE + 1, 0)
    val any = Arb.int(Int.MIN_VALUE + 1 until Int.MAX_VALUE)
  }

  object Quality {
    val regularValidRange = 0..50
    val legendaryValidRange = 80..80

    val regular = Arb.int(regularValidRange)

    val any = Arb.int(Int.MIN_VALUE + 1 until Int.MAX_VALUE)
    fun invalid(rangeToExclude: IntRange) = any.filterNot { it in rangeToExclude }

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