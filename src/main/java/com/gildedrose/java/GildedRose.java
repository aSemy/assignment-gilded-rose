package com.gildedrose.java;

import dev.adamko.gildedrose.java.item.ItemState;
import dev.adamko.gildedrose.java.item.ItemTemplateProvider;
import java.util.Arrays;

public class GildedRose {

  Item[] items;
  private final ItemTemplateProvider itemTemplateProvider;

  public GildedRose(
      Item[] items,
      ItemTemplateProvider itemTemplateProvider
  ) {
    this.items = items;
    this.itemTemplateProvider = itemTemplateProvider;
  }

  public GildedRose(Item[] items) {
    this(items, ItemTemplateProvider.defaultTemplateProvider);
  }

  public void updateQuality() {
    Arrays.stream(items).forEach(item -> {
      ItemState state = new ItemState(item);
      var update = getUpdatedItem(state);
      applyUpdate(item, update);
    });
  }

  private static void applyUpdate(Item item, ItemState update) {
    item.name = update.name();
    item.sellIn = update.sellIn();
    item.quality = update.quality();
  }

  private ItemState getUpdatedItem(ItemState item) {
    var template = itemTemplateProvider.getTemplate(item);
    return template.applyMutation(item);
  }

//  public void updateQuality() {
//    for (int i = 0; i < items.length; i++) {
//      if (!items[i].name.equals("Aged Brie")
//          && !items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
//        if (items[i].quality > 0) {
//          if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
//            items[i].quality = items[i].quality - 1;
//          }
//        }
//      } else {
//        if (items[i].quality < 50) {
//          items[i].quality = items[i].quality + 1;
//
//          if (items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
//            if (items[i].sellIn < 11) {
//              if (items[i].quality < 50) {
//                items[i].quality = items[i].quality + 1;
//              }
//            }
//
//            if (items[i].sellIn < 6) {
//              if (items[i].quality < 50) {
//                items[i].quality = items[i].quality + 1;
//              }
//            }
//          }
//        }
//      }
//
//      if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
//        items[i].sellIn = items[i].sellIn - 1;
//      }
//
//      if (items[i].sellIn < 0) {
//        if (!items[i].name.equals("Aged Brie")) {
//          if (!items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
//            if (items[i].quality > 0) {
//              if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
//                items[i].quality = items[i].quality - 1;
//              }
//            }
//          } else {
//            items[i].quality = items[i].quality - items[i].quality;
//          }
//        } else {
//          if (items[i].quality < 50) {
//            items[i].quality = items[i].quality + 1;
//          }
//        }
//      }
//    }
//  }
}
