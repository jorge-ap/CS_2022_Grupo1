package resources;

import io.cucumber.java.en.Given;

public class StepDefinitions {

    @Given("account balance is {double}")
    public void givenAccountBalance(Double initialBalance) {
        System.out.println(initialBalance);
    }

    // other step definitions

}