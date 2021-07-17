package com.gildedrose

import dev.adamko.gildedrose.item.ItemState
import dev.adamko.gildedrose.item.ItemState.Companion.asState
import dev.adamko.gildedrose.item.ItemTemplate
import dev.adamko.gildedrose.item.ItemTemplateProvider

class GildedRose(
    var items: Array<Item>,
    private val itemTemplateProvider: ItemTemplateProvider = ItemTemplateProvider.defaultTemplateProvider,
) {

  /** Convenience property for fetching a [ItemTemplate] from [itemTemplateProvider]. */
  private val ItemState.template
    get() = itemTemplateProvider.getTemplate(this)

  fun updateQuality() {
//    legacy()

    items.forEach { item ->
      val update = getUpdatedItem(item.asState())
      applyUpdate(item, update)
    }
  }

  private fun applyUpdate(item: Item, update: ItemState) {
    item.name = update.name
    item.sellIn = update.sellIn
    item.quality = update.quality
  }

  private fun getUpdatedItem(item: ItemState): ItemState {
    return item.template.applyMutation(item)
  }

//  fun legacy() {
//    for (i in items.indices) {
//      if (items[i].name != "Aged Brie" && items[i].name != "Backstage passes to a TAFKAL80ETC concert") {
//        if (items[i].quality > 0) {
//          if (items[i].name != "Sulfuras, Hand of Ragnaros") {
//            items[i].quality = items[i].quality - 1
//          }
//        }
//      } else {
//        if (items[i].quality < 50) {
//          items[i].quality = items[i].quality + 1
//
//          if (items[i].name == "Backstage passes to a TAFKAL80ETC concert") {
//            if (items[i].sellIn < 11) {
//              if (items[i].quality < 50) {
//                items[i].quality = items[i].quality + 1
//              }
//            }
//
//            if (items[i].sellIn < 6) {
//              if (items[i].quality < 50) {
//                items[i].quality = items[i].quality + 1
//              }
//            }
//          }
//        }
//      }
//
//      if (items[i].name != "Sulfuras, Hand of Ragnaros") {
//        items[i].sellIn = items[i].sellIn - 1
//      }
//
//      if (items[i].sellIn < 0) {
//        if (items[i].name != "Aged Brie") {
//          if (items[i].name != "Backstage passes to a TAFKAL80ETC concert") {
//            if (items[i].quality > 0) {
//              if (items[i].name != "Sulfuras, Hand of Ragnaros") {
//                items[i].quality = items[i].quality - 1
//              }
//            }
//          } else {
//            items[i].quality = items[i].quality - items[i].quality
//          }
//        } else {
//          if (items[i].quality < 50) {
//            items[i].quality = items[i].quality + 1
//          }
//        }
//      }
//    }
//  }
}
