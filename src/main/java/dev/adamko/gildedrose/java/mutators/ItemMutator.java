package dev.adamko.gildedrose.java.mutators;

import dev.adamko.gildedrose.java.item.ItemState;


/**
 * A Java implementation of {@link dev.adamko.gildedrose.item.ItemMutator}
 *
 * @see dev.adamko.gildedrose.item.ItemMutator
 */
@FunctionalInterface
public interface ItemMutator {

  ItemState applyMutation(ItemState item);

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemMutator#plus(dev.adamko.gildedrose.item.ItemMutator)}
   *
   * @see dev.adamko.gildedrose.item.ItemMutator#plus(dev.adamko.gildedrose.item.ItemMutator)
   */
  static ItemMutator plus(
      final ItemMutator $this,
      final ItemMutator other
  ) {
    return item -> other.applyMutation($this.applyMutation(item));
  }
}
