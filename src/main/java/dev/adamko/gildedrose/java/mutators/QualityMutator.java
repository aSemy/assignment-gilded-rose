package dev.adamko.gildedrose.java.mutators;

import dev.adamko.gildedrose.java.item.ItemState;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/**
 * A Java implementation of {@link dev.adamko.gildedrose.item.ItemMutator.QualityMutator}
 *
 * @see dev.adamko.gildedrose.item.ItemMutator.QualityMutator
 */
public class QualityMutator implements ItemMutator {

  private final IntRange range;
  private final QualityDeltaProvider qualityDeltaProvider;

  public QualityMutator(
      IntRange range,
      QualityDeltaProvider qualityDeltaProvider
  ) {
    this.range = range;
    this.qualityDeltaProvider = qualityDeltaProvider;
  }

  public ItemState applyMutation(ItemState item) {
    int delta = qualityDeltaProvider.getDelta(item);
    int newQuality = RangesKt.coerceIn(item.quality() + delta, range);
    return item.withQuality(newQuality);
  }

}
