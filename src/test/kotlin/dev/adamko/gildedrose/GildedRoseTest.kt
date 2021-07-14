package dev.adamko.gildedrose

import io.kotest.core.spec.style.BehaviorSpec

class UpdateQualityTest : BehaviorSpec({

  Given("any item") {
    When("quality is updated") {
      Then("name should not change") {

      }
    }
  }

  Given("any non-legendary item") {
    When("quality is updated") {
      Then("quality should be in range 0..50") {

      }
      Then("sellIn should change by -1") {

      }
    }
  }

  Given("any legendary item") {
    When("quality is updated") {
      Then("quality should be 80") {

      }
    }
  }

  // regular item
  Given("a regular item") {
    When("quality is updated") {
      And("is NOT expired") {
        Then("quality should change by -1") {

        }
      }
      And("is expired") {
        Then("quality should change by -2") {

        }
      }
    }
  }

  // aged item
  Given("an aged item") {
    When("quality is updated") {
      And("is NOT expired") {
        Then("quality should change by +1") {

        }
      }
      And("is expired") {
        Then("quality should change by +2") {

        }
      }
    }
  }

  // conjured item
  Given("a conjured item") {
    When("quality is updated") {
      And("is NOT expired") {
        Then("quality should change by -2") {

        }
      }
      And("is expired") {
        Then("quality should change by -4") {

        }
      }
    }
  }

  // ticket item
  Given("a ticket item") {
    When("quality is updated") {
      And("sellIn > 10") {
        Then("quality should change by +1") {

        }
      }
      And("sellIn is in 5..10") {
        Then("quality should change by +2") {

        }
      }
      And("sellIn is in 1..5") {
        Then("quality should change by +3") {

        }
      }
      And("is expired") {
        Then("quality should always be 0") {

        }
      }
    }
  }

  // legendary item
  Given("a legendary item") {
    When("quality is updated") {
      Then("nothing alters") {

      }
    }
  }

})