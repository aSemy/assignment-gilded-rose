package dev.adamko.gildedrose.gildedrosetests


import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.property.arbitrary.filter
import io.kotest.property.exhaustive.exhaustive

class TicketItems : BehaviorSpec({

  Given("a ticket item") {
    val ticketName = ItemGens.Names.tickets
    When("quality is updated") {

      // "'Backstage passes', like aged brie, increases in Quality as its SellIn value approaches"
      And("sellIn is 11 or greater") {
        val sellInGreaterThan10 = ItemGens.SellIn.any.filter { it > 10 }
        Then("quality should change by +1") {
          checkAllItems(ticketName, sellInGreaterThan10, ItemGens.Quality.any) {
            val expectedQuality = (inputQuality + 1).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      // "Quality increases by 2 when there are 10 days or less"
      And("sellIn is between 6 and 10") {
        val sellInBetween6And10 = (6..10).toList().exhaustive()
        Then("quality should change by +2") {
          checkAllItems(ticketName, sellInBetween6And10, ItemGens.Quality.any) {
            val expectedQuality = (inputQuality + 2).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      // "and by 3 when there are 5 days or less"
      And("sellIn is between 1 and 5") {
        val sellInBetween1And5 = (1..5).toList().exhaustive()
        Then("quality should change by +3") {
          checkAllItems(ticketName, sellInBetween1And5, ItemGens.Quality.any) {
            val expectedQuality = (inputQuality + 3).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      // "Quality drops to 0 after the concert"
      And("sellIn is expired (0 or lower)") {
        Then("quality should always be 0") {
          checkAllItems(ticketName, ItemGens.SellIn.expired, ItemGens.Quality.any) {
            result.quality shouldBeExactly 0
          }
        }
      }
    }
  }

})
