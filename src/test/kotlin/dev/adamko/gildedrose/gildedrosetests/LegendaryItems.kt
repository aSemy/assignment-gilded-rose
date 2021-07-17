package dev.adamko.gildedrose.gildedrosetests


import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.exhaustive.exhaustive

class LegendaryItems : BehaviorSpec({

  // legendary item
  Given("any legendary item") {
    val legendaryItemName = ItemGens.Names.legendary

    val validQualityArb = listOf(80).exhaustive()
    val invalidQualityArb = Arb.int().filterNot { it == 80 }

    listOf(
        "initial quality is valid (80)" to validQualityArb,
        "initial quality is invalid, (is not 80)" to invalidQualityArb,
    ).forEach { (desc, qualityGen) ->
      And(desc) {
        When("quality is updated") {
          Then("quality should be 80") {
            checkAllItems(legendaryItemName, ItemGens.SellIn.any, qualityGen) {
              result.quality shouldBeExactly 80
            }
          }
          // expect "nothing alters"
          xThen("quality is unaffected") {
            // test disabled, as the refactor now means that invalid (not 80) qualities are
            // forced to be 80.
            checkAllItems(legendaryItemName, ItemGens.SellIn.any, qualityGen) {
              result.quality shouldBeExactly inputQuality
            }
          }
          Then("sellIn is unaffected") {
            checkAllItems(legendaryItemName, ItemGens.SellIn.any, qualityGen) {
              result.sellIn shouldBeExactly inputSellIn
            }
          }
        }
      }
    }
  }

})
