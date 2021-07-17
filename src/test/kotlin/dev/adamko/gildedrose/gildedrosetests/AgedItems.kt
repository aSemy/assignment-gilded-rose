package dev.adamko.gildedrose.gildedrosetests

import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeExactly

class AgedItems : BehaviorSpec({

  Given("an aged item") {
    val agedItemName = ItemGens.Names.aged
    When("quality is updated") {
      And("is NOT expired") {
        val sellInGen = ItemGens.SellIn.nonExpired
        Then("quality should change by +1") {
          checkAllItems(agedItemName, sellInGen, ItemGens.Quality.any) {
            val expectedQuality = (inputQuality + 1).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      And("is expired") {
        val sellInGen = ItemGens.SellIn.expired
        Then("quality should change by +2") {
          checkAllItems(agedItemName, sellInGen, ItemGens.Quality.any) {
            val expectedQuality = (inputQuality + 2).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
    }
  }

})
