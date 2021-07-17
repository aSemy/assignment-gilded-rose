package dev.adamko.gildedrose.item


/**
 * Defines how a specific instance of an [ItemState] should be handled.
 *
 * Holds functions for the mutation of [ItemState]s.
 *
 * A template can be associated with a template with [ItemTemplateProvider].
 *
 * See [ItemTemplates] for implementations.
 *
 * @see ItemTemplates
 */
open class ItemTemplate(
    private val mutator: ItemMutator,
) : ItemMutator by mutator {

  constructor(
      vararg mutators: ItemMutator
  ) : this(
      if (mutators.isEmpty())
        throw IllegalStateException("At least one mutator is required, received none.")
      else
        mutators.asSequence().reduce(ItemMutator::plus)
  )

}
