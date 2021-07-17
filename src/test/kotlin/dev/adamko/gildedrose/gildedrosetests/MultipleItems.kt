package dev.adamko.gildedrose.gildedrosetests

import com.gildedrose.GildedRose
import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forAtLeastOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.set
import io.kotest.property.checkAll

class MultipleItems : BehaviorSpec({

  Given("multiple non-legendary items") {

    listOf(
        // test valid range
        "initial quality is inside valid range, 0..50" to ItemGens.Quality.validRegular,
        // test invalid range
        "initial quality is outside of valid range, 0..50" to ItemGens.Quality.invalidRegular,
    ).forEach { (qualityDesc, qualityGen) ->

      val itemArb = ItemGens.Binds.itemArb(
          nameGen = ItemGens.Names.nonLegendary,
          qualityGen = qualityGen
      )

      And(qualityDesc) {
        And("items are unique") {
          // create list of distinct items
          val uniqueItems = Arb.set(itemArb, 0..10)
          When("updateQuality is called") {

            Then("expect each input Item is in output, with sellIn -1") {
              checkAll(uniqueItems) { inputItems ->

                val expectedItems = inputItems.map {
                  object {
                    val expectedName = it.name
                    val expectedSellIn = it.sellIn - 1
                  }
                }

                GildedRose(inputItems.toTypedArray()).updateQuality()

                withClue("inputItems.size") {
                  inputItems shouldHaveSize expectedItems.size
                }

                expectedItems.forEach { expectedItem ->
                  withClue(
                      """
            
                        Expect input item with
                          name: ${expectedItem.expectedName}
                          sellIn: ${expectedItem.expectedSellIn}
                        is in output
                        
                        expectedItems.size: ${expectedItems.size}
                        inputItems.size: ${inputItems.size}
                          
                      """.trimIndent()
                  ) {
                    inputItems.forAtLeastOne { resultItem ->

                      resultItem should {
                        withClue("name") {
                          it.name shouldBeEqualComparingTo expectedItem.expectedName
                        }
                        withClue("sellIn") {
                          it.sellIn shouldBeExactly expectedItem.expectedSellIn
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }

        And("items are duplicated") {
          When("updateQuality is called ") {
            Then("expect each input Item is in output, with sellIn -1 * number of items") {
              checkAll(Arb.int(2..10), itemArb) { numOfItems, inputItem ->
                val expectedName = inputItem.name
                val expectedSellIn = inputItem.sellIn - numOfItems

                // create list of duplicated items
                val inputItems = Array(numOfItems) { inputItem }
                GildedRose(inputItems).updateQuality()

                withClue("expect same number of items after update") {
                  inputItems shouldHaveSize numOfItems
                }
                withClue("expect all results match the input") {
                  inputItems.forAll { resultingItem ->
                    resultingItem should {
                      it.name shouldBeEqualComparingTo expectedName
                      it.sellIn shouldBeEqualComparingTo expectedSellIn
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
})
