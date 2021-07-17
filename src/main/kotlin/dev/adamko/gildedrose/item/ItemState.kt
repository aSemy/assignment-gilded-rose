package dev.adamko.gildedrose.item

import com.gildedrose.Item
import com.gildedrose.isExpired

/**
 * Freeze the state of an [Item], so it can be used safely in other contexts without mutating
 * the original.
 */
data class ItemState(
    val name: String,
    val sellIn: Int,
    val quality: Int,
    val isExpired: Boolean,
) {

  constructor(item: Item) : this(item.name, item.sellIn, item.quality, item.isExpired())

  companion object {
    fun Item.asState(): ItemState = ItemState(this)
  }
}
