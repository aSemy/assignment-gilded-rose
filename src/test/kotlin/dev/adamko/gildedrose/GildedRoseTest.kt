package dev.adamko.gildedrose

import com.gildedrose.GildedRose
import com.gildedrose.Item
import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.property.Arb
import io.kotest.property.Gen
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.withEdgecases
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.exhaustive

class UpdateQualityTest : BehaviorSpec({

  Given("any item") {
    When("quality is updated") {
      Then("name should not change") {
        checkAllItems(ItemGens.Names.all, ItemGens.SellIn.any, ItemGens.Quality.any) {
          result.name shouldBeEqualComparingTo inputName
        }
      }
      Then("sellIn should change by -1") {
        checkAllItems(ItemGens.Names.all, ItemGens.SellIn.any, ItemGens.Quality.any) {
          result.sellIn shouldBeExactly (inputSellIn - 1)
        }
      }
    }
  }

  Given("any non-legendary item") {
    val nonLegendaryItemName = ItemGens.Names.nonLegendary

    listOf(
      "initial quality is inside valid range, 0..50" to ItemGens.Quality.regular,
      // TODO add validation/handling of items outside of valid range
//        "initial quality is outside of valid range, 0..50" to ItemGens.Quality.invalid(0..50),
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

  // TODO test invalid qualities...
  val validRegularQualityArb = ItemGens.Quality.regular

  // regular item
  Given("a regular item") {
    val regularItemName = ItemGens.Names.regular
    When("quality is updated") {
      And("is NOT expired") {
        val sellInGen = ItemGens.SellIn.nonExpired
        Then("quality should change by -1") {
          checkAllItems(regularItemName, sellInGen, validRegularQualityArb) {
            val expectedQuality = (inputQuality - 1).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      And("is expired") {
        val sellInGen = ItemGens.SellIn.expired
        Then("quality should change by -2") {
          checkAllItems(regularItemName, sellInGen, validRegularQualityArb) {
            val expectedQuality = (inputQuality - 2).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
    }
  }

  // aged item
  Given("an aged item") {
    val agedItemName = ItemGens.Names.aged
    When("quality is updated") {
      And("is NOT expired") {
        val sellInGen = ItemGens.SellIn.nonExpired
        Then("quality should change by +1") {
          checkAllItems(agedItemName, sellInGen, validRegularQualityArb) {
            val expectedQuality = (inputQuality + 1).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      And("is expired") {
        val sellInGen = ItemGens.SellIn.expired
        Then("quality should change by +2") {
          checkAllItems(agedItemName, sellInGen, validRegularQualityArb) {
            val expectedQuality = (inputQuality + 2).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
    }
  }

  // conjured item
  // TODO implement conjured items
  xGiven("a conjured item") {
    val conjuredItemName = ItemGens.Names.conjured
    When("quality is updated") {
      And("is NOT expired") {
        val sellInGen = ItemGens.SellIn.nonExpired
        Then("quality should change by -2") {
          checkAllItems(conjuredItemName, sellInGen, validRegularQualityArb) {
            val expectedQuality = (inputQuality - 2).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      And("is expired") {
        val sellInGen = ItemGens.SellIn.expired
        Then("quality should change by - 4") {
          checkAllItems(conjuredItemName, sellInGen, validRegularQualityArb) {
            val expectedQuality = (inputQuality - 4).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
    }
  }

  // ticket item
  Given("a ticket item") {
    val ticketName = ItemGens.Names.tickets
    When("quality is updated") {

      // "'Backstage passes', like aged brie, increases in Quality as its SellIn value approaches"
      And("sellIn is 11 or greater") {
        val sellInGreaterThan10 = Arb.int().filter { it > 10 }.withEdgecases(11)
        Then("quality should change by +1") {
          checkAllItems(ticketName, sellInGreaterThan10, validRegularQualityArb) {
            val expectedQuality = (inputQuality + 1).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      // "Quality increases by 2 when there are 10 days or less"
      And("sellIn is between 6 and 10") {
        val sellInBetween5And10 = (6..10).toList().exhaustive()
        Then("quality should change by +2") {
          checkAllItems(ticketName, sellInBetween5And10, validRegularQualityArb) {
            val expectedQuality = (inputQuality + 2).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      // "and by 3 when there are 5 days or less"
      And("sellIn is between 1 and 5") {
        val sellInBetween1And4 = (1..5).toList().exhaustive()
        Then("quality should change by +3") {
          checkAllItems(ticketName, sellInBetween1And4, validRegularQualityArb) {
            val expectedQuality = (inputQuality + 3).coerceIn(ItemGens.Quality.regularValidRange)
            result.quality shouldBeExactly expectedQuality
          }
        }
      }
      // "Quality drops to 0 after the concert"
      And("sellIn is expired (0 or lower)") {
        Then("quality should always be 0") {
          checkAllItems(ticketName, ItemGens.SellIn.expired, validRegularQualityArb) {
            result.quality shouldBeExactly 0
          }
        }
      }
    }
  }

  // legendary item
  Given("any legendary item") {
    val legendaryItemName = ItemGens.Names.legendary
    listOf(
      "initial quality is valid (80)" to listOf(80).exhaustive(),
      // TODO add validation/handling of items outside of valid range
//        "initial quality is invalid, (is not 80)" to Arb.int().filterNot { it == 80 },
    ).forEach { (desc, qualityGen) ->
      And(desc) {
        When("quality is updated") {
          Then("quality should be 80") {
            checkAllItems(legendaryItemName, ItemGens.SellIn.any, qualityGen) {
              result.quality shouldBeExactly 80
            }
          }
          // expect "nothing alters"
          Then("quality is unaffected") {
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

}) {

  companion object {

    suspend inline fun checkAllItems(
      nameGen: Gen<String>,
      sellInGen: Gen<Int>,
      qualityGen: Gen<Int>,
      repetitionsGen: Gen<Int> = listOf(1).exhaustive(),
      crossinline assert: AssertItemContext.() -> Unit
    ) {
      checkAll(
        nameGen,
        sellInGen,
        qualityGen,
        repetitionsGen,
      ) { inputName: String, inputSellIn: Int, inputQuality: Int, inputRepetitions: Int ->
        val result = createAndUpdateItem(inputName, inputSellIn, inputQuality, inputRepetitions)
        withClue(
          """
              
              inputs:
                name: $inputName
                sellIn: $inputSellIn
                quality: $inputQuality
                repetitions: $inputRepetitions
              output:
                item: $result
              
            """.trimIndent()
        ) {
          AssertItemContext(inputName, inputSellIn, inputQuality, inputRepetitions, result).assert()
        }
      }
    }

    fun createAndUpdateItem(name: String, sellIn: Int, quality: Int, repetitions: Int): Item {
      val item = Item(name, sellIn, quality)
      val gildedRose = GildedRose(arrayOf(item))
      repeat(repetitions) { gildedRose.updateQuality() }
      return item
    }

    data class AssertItemContext(
      val inputName: String,
      val inputSellIn: Int,
      val inputQuality: Int,
      val inputRepetitions: Int,
      val result: Item,
    )
  }
}
