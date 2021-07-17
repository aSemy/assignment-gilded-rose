package dev.adamko.config

import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.Gen
import io.kotest.property.arbitrary.merge

object KotestExtensions {

  inline fun <A, reified B : A> List<Gen<B>>.mergeAll(): Arb<A> =
    map { it.toArb() }
      .reduce { acc, gen -> acc.apply { merge(gen) } }

  inline fun <reified T> Gen<T>.toArb() = when (this) {
    is Arb<T>        -> this
    is Exhaustive<T> -> toArb()
  }
}
