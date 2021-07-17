package dev.adamko.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.Listener
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter

import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.Gen
import io.kotest.property.arbitrary.IntShrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.merge
import kotlin.random.nextInt

class KotestConfig : AbstractProjectConfig() {
  override fun listeners(): List<Listener> = listOf(
      JunitXmlReporter(
          includeContainers = false,
          useTestPathAsName = true,
      ),
      HtmlReporter("reports/kotest")
  )

  companion object {

    inline fun <A, reified B : A> List<Gen<B>>.mergeAll(): Arb<A> =
        map { it.toArb() }
            .reduce { acc, gen -> acc.apply { merge(gen) } }

    inline fun <reified T> Gen<T>.toArb() = when (this) {
      is Arb<T>        -> this
      is Exhaustive<T> -> toArb()
    }

    fun Arb.Companion.intEdgecases(
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE,
        additionalEdgecases: List<Int> = emptyList(),
    ) = Arb.intEdgecases(min..max, additionalEdgecases)

    /**  Helper method to initialise a [Arb.Companion.int] with some edgecases. */
    fun Arb.Companion.intEdgecases(
        range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
        additionalEdgecases: List<Int> = emptyList(),
    ): Arb<Int> {
      val edgecases =
          (
              listOf(-1, 0, 1, range.first, range.last, Int.MIN_VALUE, Int.MAX_VALUE) +
              additionalEdgecases
          ).filter { it in range }
      return arbitrary(edgecases, IntShrinker(range)) { it.random.nextInt(range) }
    }
  }
}
