package dev.adamko.gildedrose.java.item;

import com.gildedrose.java.Item;
import org.jetbrains.annotations.NotNull;


/**
 * A Java implementation of {@link dev.adamko.gildedrose.item.ItemState}
 *
 * @see dev.adamko.gildedrose.item.ItemState
 */
public record ItemState(
    @NotNull
    String name,
    int sellIn,
    int quality,
    boolean isExpired
) {

  public ItemState(Item item) {
    this(item.name, item.sellIn, item.quality, isItemExpired(item));
  }

  public ItemState withQuality(int quality) {
    return new ItemState(this.name, this.sellIn, quality, this.isExpired);
  }

  public ItemState withSellIn(int sellIn) {
    return new ItemState(this.name, sellIn, this.quality, this.isExpired);
  }

  private static boolean isItemExpired(Item item) {
    return item.sellIn <= 0;
  }

}
