package dev.adamko.gildedrose.gildedrosetests


import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeExactly

class RegularItems : BehaviorSpec({

  // regular item
  Given("a regular item") {
    val regularItemName = ItemGens.Names.regular
    When("quality is updated") {
      And("is NOT expired") {
        val sellInGen = ItemGens.SellIn.nonExpired
        Then("quality should change by -1") {
          checkAllItems(regularItemName, sellInGen, ItemGens.Quality.any) {
            val expectedQuality = (inputQuality - 1).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      And("is expired") {
        val sellInGen = ItemGens.SellIn.expired
        Then("quality should change by -2") {
          checkAllItems(regularItemName, sellInGen, ItemGens.Quality.any) {
            val expectedQuality = (inputQuality - 2).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
    }
  }

})
