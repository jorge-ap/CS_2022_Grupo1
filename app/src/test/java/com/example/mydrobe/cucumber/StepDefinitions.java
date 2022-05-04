package com.example.mydrobe.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;
import io.cucumber.java.en.Given;

@RunWith(Cucumber.class)
public class StepDefinitions {

    @Given("account balance is {double}")
    public void givenAccountBalance(Double initialBalance) {
        System.out.println(initialBalance);
    }

    // other step definitions

}