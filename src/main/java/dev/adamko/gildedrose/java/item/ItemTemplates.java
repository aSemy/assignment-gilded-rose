package dev.adamko.gildedrose.java.item;

import dev.adamko.gildedrose.java.mutators.ItemMutator;
import dev.adamko.gildedrose.java.mutators.ItemMutators;
import dev.adamko.gildedrose.java.mutators.QualityDeltaProvider;
import dev.adamko.gildedrose.java.mutators.QualityMutator;
import kotlin.jvm.functions.Function1;
import kotlin.ranges.IntRange;


/**
 * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates}
 *
 * @see dev.adamko.gildedrose.item.ItemTemplates
 */
public class ItemTemplates {

  private ItemTemplates() {
  }

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates#getRegularSellInDecrement()}
   *
   * @see dev.adamko.gildedrose.item.ItemTemplates#getRegularSellInDecrement()
   */
  public static final ItemMutator regularSellInDecrement =
      item -> item.withSellIn(item.sellIn() - 1);

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates#getLegendaryItem()}
   *
   * @see dev.adamko.gildedrose.item.ItemTemplates#getLegendaryItem()
   */
  public static final ItemTemplate legendaryItem = new ItemTemplate(
      ItemMutators.constantQualityMutator(80)
  );

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates#getRegularItemValidRange()}
   *
   * @see dev.adamko.gildedrose.item.ItemTemplates#getRegularItemValidRange()
   */
  private static final IntRange regularItemValidQualityRange = new IntRange(0, 50);

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates#qualityAdjustingItemTemplate(Function1)}
   *
   * @see dev.adamko.gildedrose.item.ItemTemplates#qualityAdjustingItemTemplate(Function1)
   */
  private static ItemTemplate createQualityAdjustingItemTemplate(
      QualityDeltaProvider qualityDeltaProvider
  ) {
    return new ItemTemplate(
        new QualityMutator(regularItemValidQualityRange, qualityDeltaProvider),
        regularSellInDecrement
    );
  }

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates#getRegularItem()}
   *
   * @see dev.adamko.gildedrose.item.ItemTemplates#getRegularItem()
   */
  public static final ItemTemplate regularItem =
      createQualityAdjustingItemTemplate(item -> item.isExpired() ? -2 : -1);

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates#getAgedItem()}
   *
   * @see dev.adamko.gildedrose.item.ItemTemplates#getAgedItem()
   */
  public static final ItemTemplate agedItem =
      createQualityAdjustingItemTemplate(item -> item.isExpired() ? 2 : 1);

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates#getConjuredItem()}
   *
   * @see dev.adamko.gildedrose.item.ItemTemplates#getConjuredItem()
   */
  public static final ItemTemplate conjuredItem =
      createQualityAdjustingItemTemplate(item -> item.isExpired() ? -4 : -2);

  /**
   * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplates#getTicketItem()}
   *
   * @see dev.adamko.gildedrose.item.ItemTemplates#getTicketItem()
   */
  public static final ItemTemplate ticketItem = new ItemTemplate(
      ItemMutators.mutatorSelector(item -> {

        var expiredMutator = ItemMutators.constantQualityMutator(0);

        var nonExpiredMutator = new QualityMutator(new IntRange(0, 50), it -> {
          if (item.sellIn() <= 5) {
            return 3;
          } else if (item.sellIn() <= 10) {
            return 2;
          } else {
            return 1;
          }
        });

        if (item.isExpired()) {
          return expiredMutator;
        } else {
          return nonExpiredMutator;
        }
      }),
      regularSellInDecrement
  );
}
