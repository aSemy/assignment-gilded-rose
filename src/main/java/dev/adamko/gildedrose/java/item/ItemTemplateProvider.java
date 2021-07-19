package dev.adamko.gildedrose.java.item;

import dev.adamko.gildedrose.item.ItemTemplateProvider.Companion;
import kotlin.text.StringsKt;


/**
 * A Java implementation of {@link dev.adamko.gildedrose.item.ItemTemplateProvider}
 *
 * @see dev.adamko.gildedrose.item.ItemTemplateProvider
 */
@FunctionalInterface
public interface ItemTemplateProvider {

  ItemTemplate getTemplate(ItemState item);

  /**
   * A Java implementation of {@link Companion#getDefaultTemplateProvider()}
   *
   * @see Companion#getDefaultTemplateProvider()
   */
  ItemTemplateProvider defaultTemplateProvider = item -> {

    // conjured
    if (StringsKt.startsWith(item.name(), "Conjured", false)) {
      return ItemTemplates.conjuredItem;
    }
    // tickets
    else if ("Backstage passes to a TAFKAL80ETC concert".equals(item.name())) {
      return ItemTemplates.ticketItem;
    }
    // legendary
    else if ("Sulfuras, Hand of Ragnaros".equals(item.name())) {
      return ItemTemplates.legendaryItem;
    }
    // aged
    else if ("Aged Brie".equals(item.name())) {
      return ItemTemplates.agedItem;
    }
    // regular
    else {
      return ItemTemplates.regularItem;
    }

  };

}
