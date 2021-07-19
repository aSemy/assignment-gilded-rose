package dev.adamko.gildedrose.java.mutators;

import dev.adamko.gildedrose.java.item.ItemState;
import kotlin.jvm.functions.Function1;


/**
 * For use by {@link dev.adamko.gildedrose.java.mutators.QualityMutator}.
 * <p>
 * The Kotlin equivalent is the parameter of {@link dev.adamko.gildedrose.item.ItemTemplates#qualityAdjustingItemTemplate(Function1)}
 *
 * @see dev.adamko.gildedrose.item.ItemTemplates#qualityAdjustingItemTemplate(Function1)
 * @see dev.adamko.gildedrose.java.mutators.QualityMutator
 */
@FunctionalInterface
public interface QualityDeltaProvider {

  Integer getDelta(ItemState item);

}
