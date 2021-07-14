package dev.adamko.gildedrose

import dev.adamko.gildedrose.testdata.ItemGens
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import io.kotest.property.checkAll

class ItemTest : FunSpec({

  test("expect Item.toString() contains all item data") {
    checkAll(ItemGens.Binds.itemArb()) { item ->
      item.toString() should {
        it shouldContain item.name
        it shouldContain "${item.sellIn}"
        it shouldContain "${item.quality}"
      }
    }
  }
})
