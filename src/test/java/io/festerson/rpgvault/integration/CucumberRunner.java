package io.festerson.rpgvault.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/resources/features", glue = {"io.festerson.rpgvault"})
public class CucumberRunner {
}
