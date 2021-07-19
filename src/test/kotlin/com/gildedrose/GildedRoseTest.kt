package com.gildedrose

import dev.adamko.gildedrose.GildedRoseDelegate
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo

class GildedRoseTest : AnnotationSpec() {

  @Test
  fun `expect name is unaffected by updateQuality()`() {
    val inputName = "foo"

    val items = arrayOf(Item("foo", 0, 0))
    val app = GildedRoseDelegate(items)
    app.updateQuality()
    items[0].name shouldBeEqualComparingTo inputName
  }

}
