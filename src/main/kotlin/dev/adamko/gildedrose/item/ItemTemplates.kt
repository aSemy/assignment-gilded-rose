package dev.adamko.gildedrose.item

import com.gildedrose.Item
import dev.adamko.gildedrose.item.ItemMutator.Companion.constantQualityMutator
import dev.adamko.gildedrose.item.ItemMutator.Companion.mutatorSelector


object ItemTemplates {

  /** Decrease sellIn by `-1` */
  private val regularSellInDecrement = ItemMutator { it.copy(sellIn = it.sellIn - 1) }

  /** > "Sulfuras", being a legendary item, never has to be sold or decreases in Quality */
  val legendaryItem = ItemTemplate(
      constantQualityMutator(80),
  )

  /** Regular, non-legendary, items have a valid range of 0 to 50 (inclusive). */
  private val regularItemValidRange = 0..50

  /**
   * Create a [ItemTemplate] for an [Item]. The resulting template:
   *
   *  - adjusts [Item.quality] using the delta from [qualityDeltaProvider]
   *  - adjust [Item.sellIn] using [regularSellInDecrement]
   */
  private fun qualityAdjustingItemTemplate(
      qualityDeltaProvider: (ItemState) -> Int,
  ) = ItemTemplate(
      ItemMutator.QualityMutator(regularItemValidRange, qualityDeltaProvider),
      regularSellInDecrement,
  )

  /** > "Once the sell by date has passed, Quality degrades twice as fast" */
  val regularItem = qualityAdjustingItemTemplate { item ->
    when {
      item.isExpired -> -2
      else           -> -1
    }
  }

  /** > "Aged Brie" actually increases in Quality the older it gets */
  val agedItem = qualityAdjustingItemTemplate { item ->
    when {
      item.isExpired -> 2
      else           -> 1
    }
  }

  /** > "Conjured" items degrade in Quality twice as fast as normal items */
  val conjuredItem = qualityAdjustingItemTemplate { item ->
    when {
      item.isExpired -> -4
      else           -> -2
    }
  }

  /**
   * > "Backstage passes", like aged brie, increases in Quality as its SellIn value approaches;
   * >
   * > Quality
   * > * increases by 2 when there are 10 days or less
   * > * and by 3 when there are 5 days or less
   * > * but drops to 0 after the concert
   *
   * Uses a [mutatorSelector] to set quality to 0 if the item is expired, else it will mutate
   * using a [ItemMutator.QualityMutator].
   */
  val ticketItem = run {

    val expiredMutator = constantQualityMutator(0)

    val nonExpiredMutator = ItemMutator.QualityMutator(regularItemValidRange) { item ->
      require(!item.isExpired) { "expired tickets should be handled by 'expiredMutator'" }
      when {
        // "increases by 3 when there are 5 days or less"
        item.sellIn <= 5  -> 3
        // "increases by 2 when there are 10 days or less"
        item.sellIn <= 10 -> 2
        // "increases in Quality as its SellIn approaches"
        else              -> 1
      }
    }

    // select the expired/non-expired mutator based on Item expiration
    val ticketMutator = mutatorSelector { item ->
      when {
        item.isExpired -> expiredMutator
        else           -> nonExpiredMutator
      }
    }

    // combine the quality mutator with a regular sellIn decrement
    ItemTemplate(ticketMutator + regularSellInDecrement)
  }
}
