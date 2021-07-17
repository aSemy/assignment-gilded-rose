package dev.adamko.gildedrose.item

import dev.adamko.gildedrose.item.ItemMutator.Companion.noopMutator


/**
 * Given an [ItemState], mutate it (or not, see [noopMutator]) and return a new [ItemState].
 *
 * See [ItemTemplates] for implementations.
 *
 * Multiple mutators can be combined using [ItemMutator.plus].
 *
 * @see ItemTemplates
 */
fun interface ItemMutator {

  fun applyMutation(item: ItemState): ItemState

  /** Create a new [ItemMutator] that first applies this mutation, and then [other]. */
  operator fun plus(other: ItemMutator): ItemMutator =
      ItemMutator { item -> other.applyMutation(applyMutation(item)) }

  /** Returns a new [ItemState] with an adjusted [ItemState.quality]. */
  class QualityMutator(
      private val range: IntRange,
      val qualityDeltaProvider: (ItemState) -> Int
  ) : ItemMutator {

    override fun applyMutation(item: ItemState): ItemState {
      val delta = qualityDeltaProvider(item)
      val newQuality = (item.quality + delta).coerceIn(range)
      return item.copy(quality = newQuality)
    }

  }

  companion object {

    /** Does nothing  */
    val noopMutator = ItemMutator { it }

    /** Sets the quality to be a constant number. */
    fun constantQualityMutator(constantQuality: Int) =
        ItemMutator { it.copy(quality = constantQuality) }

    /** Select between different [ItemMutator]s, based on an [ItemState]. */
    fun mutatorSelector(selector: (ItemState) -> ItemMutator) =
        ItemMutator { item -> selector(item).applyMutation(item) }

  }
}
