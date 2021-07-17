package dev.adamko.gildedrose.gildedrosetests

import com.gildedrose.isExpired
import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.exhaustive.exhaustive

class AnyItem : BehaviorSpec({

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

    data class IsExpiredTestCase(
        val inputSellIn: Int,
        val expectedSellIn: Int,
        val expectedExpiration: Boolean
    )

    listOf(
        IsExpiredTestCase(4, 3, false),
        IsExpiredTestCase(3, 2, false),
        IsExpiredTestCase(2, 1, false),
        IsExpiredTestCase(1, 0, true),
        IsExpiredTestCase(0, -1, true),
        IsExpiredTestCase(-1, -2, true),
        IsExpiredTestCase(-2, -3, true),
    ).forEach {
      it.run {
        val sellInArb = listOf(inputSellIn).exhaustive()

        And("sellIn is $inputSellIn") {
          When("quality is updated") {
            Then("expect sellIn is $expectedSellIn") {
              checkAllItems(ItemGens.Names.all, sellInArb, ItemGens.Quality.any) {
                result.sellIn shouldBeExactly expectedSellIn
              }
            }
            Then("expect item.isExpired() is $expectedExpiration") {
              checkAllItems(ItemGens.Names.all, sellInArb, ItemGens.Quality.any) {
                result.isExpired() shouldBe expectedExpiration
              }
            }
          }
        }
      }
    }
  }
})
