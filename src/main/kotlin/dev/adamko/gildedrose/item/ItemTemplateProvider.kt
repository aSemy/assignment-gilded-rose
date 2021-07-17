package dev.adamko.gildedrose.item


/**
 * Provides an [ItemTemplate] for a given [ItemState].
 */
fun interface ItemTemplateProvider {

  fun getTemplate(item: ItemState): ItemTemplate

  companion object {

    val defaultTemplateProvider = ItemTemplateProvider {
      it.name.run {
        when {
          // conjured
          startsWith("Conjured")                              -> ItemTemplates.conjuredItem
          // tickets
          equals("Backstage passes to a TAFKAL80ETC concert") -> ItemTemplates.ticketItem
          // legendary
          equals("Sulfuras, Hand of Ragnaros")                -> ItemTemplates.legendaryItem
          // aged
          equals("Aged Brie")                                 -> ItemTemplates.agedItem
          // regular
          else                                                -> ItemTemplates.regularItem
        }
      }
    }
  }
}
