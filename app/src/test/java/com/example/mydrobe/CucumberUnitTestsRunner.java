package com.example.mydrobe;

import org.junit.runner.RunWith;

import io.cucumber.android.runner.CucumberAndroidJUnitRunner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@CucumberOptions(
        plugin = "pretty",
        features = "src/test/cucumberTests/features",
        glue = "com.example.mydrobe"
)
@RunWith(Cucumber.class)
public class CucumberUnitTestsRunner extends CucumberAndroidJUnitRunner {

}