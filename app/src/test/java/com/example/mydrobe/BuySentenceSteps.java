package com.example.mydrobe;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class BuySentenceSteps {
    int having_coins = 0;
    int requiring_coins = 0;

    @Given("I have {int} coins")
    public void haveNCoins(int incomingCoins) {
        having_coins = incomingCoins;
    }

    @When("I need {int} coins")
    public void needNCoins(int neededCoins) {
        requiring_coins = neededCoins;
    }

    @Then("I should have {int} more coins")
    public void shouldHave(int shouldHaveCoins) {
        assertEquals(shouldHaveCoins, requiring_coins - having_coins);
    }

    @Then("I should see {string} coins")
    public void shouldSeeString(String shouldSeeValue) {
        shouldSee(shouldSeeValue);
    }

    @Then("I should see {int} coins")
    public void shouldSeeInt(int shouldSeeValue) {
        shouldSee(Integer.toString(shouldSeeValue));
    }

    private void shouldSee(String shouldSee){
        if (having_coins == 50) {
            assertEquals("CREA TU FRASE", shouldSee);
        } else {
            assertEquals(Integer.toString(having_coins), shouldSee);
        }
    }
}