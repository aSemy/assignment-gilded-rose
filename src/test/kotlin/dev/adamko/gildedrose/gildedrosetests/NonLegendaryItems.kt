package dev.adamko.gildedrose.gildedrosetests

import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeInRange

class NonLegendaryItems : BehaviorSpec({

  Given("any non-legendary item") {
    val nonLegendaryItemName = ItemGens.Names.nonLegendary

    listOf(
        // test valid range
        "initial quality is inside valid range, 0..50" to ItemGens.Quality.validRegular,
        // test invalid range
        "initial quality is outside of valid range, 0..50" to ItemGens.Quality.invalidRegular,
    ).forEach { (desc, qualityGen) ->

      And(desc) {
        When("quality is updated") {
          Then("quality should be in range 0..50") {
            checkAllItems(nonLegendaryItemName, ItemGens.SellIn.any, qualityGen) {
              result.quality shouldBeInRange 0..50
            }
          }
        }
      }
    }
  }

})
