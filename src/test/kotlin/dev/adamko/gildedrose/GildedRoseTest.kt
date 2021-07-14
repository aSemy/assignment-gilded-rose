package dev.adamko.gildedrose

import com.gildedrose.GildedRose
import com.gildedrose.Item
import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.property.Gen
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
    }
  }

  Given("any non-legendary item") {
    val nonLegendaryItemName = ItemGens.Names.nonLegendary
    When("quality is updated") {
      Then("quality should be in range 0..50") {
        checkAllItems(nonLegendaryItemName, ItemGens.SellIn.any, ItemGens.Quality.any) {
          result.quality shouldBeInRange 0..50
        }
      }
      Then("sellIn should change by -1") {
        checkAllItems(nonLegendaryItemName, ItemGens.SellIn.any, ItemGens.Quality.any) {
          result.sellIn shouldBeExactly (inputSellIn - 1)
        }
      }
    }
  }

  Given("any legendary item") {
    val legendaryItemName = ItemGens.Names.legendary
    When("quality is updated") {
      Then("quality should be 80") {
        checkAllItems(legendaryItemName, ItemGens.SellIn.any, ItemGens.Quality.any) {
          result.quality shouldBeExactly 80
        }
      }
    }
  }

  // regular item
  Given("a regular item") {
    When("quality is updated") {
      And("is NOT expired") {
        Then("quality should change by -1") {

        }
      }
      And("is expired") {
        Then("quality should change by -2") {

        }
      }
    }
  }

  // aged item
  Given("an aged item") {
    When("quality is updated") {
      And("is NOT expired") {
        Then("quality should change by +1") {

        }
      }
      And("is expired") {
        Then("quality should change by +2") {

        }
      }
    }
  }

  // conjured item
  Given("a conjured item") {
    When("quality is updated") {
      And("is NOT expired") {
        Then("quality should change by -2") {

        }
      }
      And("is expired") {
        Then("quality should change by -4") {

        }
      }
    }
  }

  // ticket item
  Given("a ticket item") {
    When("quality is updated") {
      And("sellIn > 10") {
        Then("quality should change by +1") {

        }
      }
      And("sellIn is in 5..10") {
        Then("quality should change by +2") {

        }
      }
      And("sellIn is in 1..5") {
        Then("quality should change by +3") {

        }
      }
      And("is expired") {
        Then("quality should always be 0") {

        }
      }
    }
  }

  // legendary item
  Given("a legendary item") {
    When("quality is updated") {
      Then("nothing alters") {

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
        AssertItemContext(inputName, inputSellIn, inputQuality, inputRepetitions, result).assert()
      }
    }

    fun createAndUpdateItem(name: String, sellIn: Int, quality: Int, repetitions: Int): Item {
      val item = Item(name, sellIn, quality)
      val gildedRose = GildedRose(arrayOf(item));
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