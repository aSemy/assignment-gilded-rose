package dev.adamko.gildedrose.java.item;

import dev.adamko.gildedrose.java.mutators.ItemMutator;
import java.util.Arrays;
import kotlin.collections.CollectionsKt;


/**
 * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplate}
 *
 * @see dev.adamko.gildedrose.item.ItemTemplate
 */
public class ItemTemplate implements ItemMutator {

  private final ItemMutator mutator;

  public ItemTemplate(ItemMutator... mutators) {
    assert mutators.length > 0 : "At least one mutator is required, received none.";
    this.mutator = CollectionsKt.reduce(Arrays.asList(mutators), ItemMutator::plus);
  }

  @Override
  public ItemState applyMutation(ItemState item) {
    return mutator.applyMutation(item);
  }
}
