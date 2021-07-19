package dev.adamko.gildedrose

import com.gildedrose.GildedRose as GildedRoseKt
import com.gildedrose.Item as ItemKt
import com.gildedrose.java.GildedRose as GildedRoseJvm
import com.gildedrose.java.Item as ItemJvm

/**
 * All test classes use this delegate. It calls either [GildedRoseKt] or [GildedRoseJvm], based
 * on the value of [useJvm].
 */
class GildedRoseDelegate(
    private val itemsKt: Array<ItemKt>,
    private val useJvm: Boolean = true
) {

  private val itemsJvm =
      itemsKt
          .groupingBy { it } // itemsKt might have repeated items, so emulate the same in itemsJvm
          .eachCount()
          .flatMap { (itemKt, count) ->
            val itemJvm = itemKt.toJvm()
            List(count) { itemJvm }
          }
          .toTypedArray()

  private val grKt = GildedRoseKt(itemsKt)
  private val grJvm = GildedRoseJvm(itemsJvm)

  fun updateQuality() {

    if (useJvm) {
      grJvm.updateQuality()
      itemsKt.forEachIndexed { i, original ->
        itemsJvm[i].let {
          original.name = it.name
          original.sellIn = it.sellIn
          original.quality = it.quality
        }
      }
    } else {
      grKt.updateQuality()
    }
  }

  companion object {
    private fun ItemKt.toJvm() = ItemJvm(name, sellIn, quality)
  }

}
