package dev.adamko.gildedrose.java.mutators;

import dev.adamko.gildedrose.item.ItemMutator.Companion;
import dev.adamko.gildedrose.java.item.ItemState;
import java.util.function.Function;

/**
 * A Java implementation of {@link dev.adamko.gildedrose.item.ItemMutator.QualityMutator}
 *
 * @see dev.adamko.gildedrose.item.ItemMutator
 */
public class ItemMutators {

  /**
   * A Java implementation of {@link Companion#constantQualityMutator(int)}
   *
   * @see Companion#constantQualityMutator(int)
   */
  public static ItemMutator constantQualityMutator(final int constantQuality) {
    return item -> item.withQuality(constantQuality);
  }

  /**
   * A Java implementation of {@link Companion#mutatorSelector}
   *
   * @see Companion#mutatorSelector
   */
  public static ItemMutator mutatorSelector(final Function<ItemState, ItemMutator> selector) {
    return item -> selector.apply(item).applyMutation(item);
  }

}
