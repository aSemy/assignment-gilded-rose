package dev.adamko.gildedrose.gildedrosetests

import com.gildedrose.Item
import dev.adamko.gildedrose.GildedRoseDelegate
import io.kotest.assertions.withClue
import io.kotest.property.Gen
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.exhaustive

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
  val gildedRose = GildedRoseDelegate(arrayOf(item))
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
