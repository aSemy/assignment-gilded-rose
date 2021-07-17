package com.gildedrose

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo

class GildedRoseTest : AnnotationSpec() {

  @Test
  fun `expect name is unaffected by updateQuality()`() {
    val inputName = "foo"

    val items = arrayOf(Item("foo", 0, 0))
    val app = GildedRose(items)
    app.updateQuality()
    app.items[0].name shouldBeEqualComparingTo inputName
  }

}
