### Run

Requirements:

* jdk 16

```shell
./gradlew build
```

Unit tests:

```shell
./gradlew test
```

(also generates a Jacoco test coverage report)

PIT testing:

```shell
./gradlew pitest
```

#### Test reports

These are available in the build directory `./build/reports/`

Example test reports are available:

* [Unit tests](./examplereports/kotest/index.html)
* [Jacoco](./examplereports/jacoco/test/html/index.html)
* [PIT test](./examplereports/pitest/index.html)

### Overview

The code has been refactored, and Conjured items implemented.

I have implemented nearly 100% test coverage, and mutation testing.

#### Code

These are the changes I have made.

Original code (`src/main/kotlin/com/gildedrose/`)

* `GildedRose.kt`

  Altered to accept a `ItemTemplateProvider`.

  When `updateQuality()` is called, a loop iterates over `items`.

  For each `Item`, `ItemTemplateProvider` provides an `ItemTemplate`, which in turn provides an
  updated copy. The `applyUpdate()` method applies this update to the original.

* `Item.kt`

  A utility extension function, `isExpired()`, has been added to the file. When the Item has 0 or
  fewer `sellIn` days, it returns `true`.

Additional code (`src/main/kotlin/dev/adamko/gildedrose/`)

* `ItemMutator.kt`

  Implementations of this interface provide a mutated copy of an input `ItemState`. The copy can be
  mutated in any way.

    * `QualityMutator` - a quality specific implementation of `ItemMutator` that returns an
      updated `ItemState` with an altered Quality.

* `ItemState.kt`

  A frozen copy of an `Item`. Used to prevent accidental mutation of the source `Item` during
  processing.

* `ItemTemplate.kt`

  A linking class between `ItemMutator`s and an `Item`. It is associated with an `Item` by an
  implementation of `ItemTemplateProvider`.

* `ItemTemplateProvider.kt`

  Associates an `Item` with an `ItemTemplate`. I have provided a default that implements existing
  behaviour.

* `ItemTemplates.kt`

  Implementations of `ItemTemplates` for regular, aged, conjured, ticket, and legendary items.

#### Testing

I primarily used Behaviour driven testing (given, when, then). Tests for each specific or
generalised type of item are in `src/test/kotlin/dev/adamko/gildedrose/gildedrosetests`

I implemented Property Based Testing for this application using [Kotest](https://kotest.io/). Shared
data generators are in `src/test/kotlin/dev/adamko/gildedrose/testdata/ItemGens.kt`

To verify test coverage, I used [PIT](https://pitest.org/) mutation testing. This mutates the main
source code and then runs the tests. If the test still passes, then this is a bad outcome, and PIT
will report an error.

### Undocumented behaviour

Some behaviour of Gilded Rose was undocumented. I have made notes of this and how I handled it.

#### Integer overflow

If sellIn is at `Int.MIN_VALUE` then `updateQuality` won't update sellIn, due to an Integer
overflow.

Decision: I have decided to avoid this for now. I have altered the tests to avoid reaching the
overflow.

To properly resolve this, I would either introduce validation (to ignore invalid items, or throw an
exception), or use a more flexible dates system (change sellIn to be a Java 8 LocalDate).

#### Duplicated items

If an item is in `GildedRose.items` multiple times, when `updateQuality` runs then that item will
have its quality updated multiple times.

Decision: I have retained this behaviour, and added a test to verify it.

#### Quality exceeds limits

`updateItems` doesn't validate/force quality to be in the expected range.

E.g., given a legendary item with quality 100, the output will be 100, outside the allowed valid of

80.

Decision: force quality to be inside expected range. If a legendary item of quality 100 is updated,
then `updateQuality` will set quality to be 80.
