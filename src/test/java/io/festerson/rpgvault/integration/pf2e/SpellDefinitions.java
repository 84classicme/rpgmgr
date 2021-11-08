package io.festerson.rpgvault.integration.pf2e;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.festerson.rpgvault.domain.pf2api.SpellResponse;
import io.festerson.rpgvault.integration.CucumberTest;
import io.festerson.rpgvault.pf2e.PF2eService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

public class SpellDefinitions extends CucumberTest {

    @Autowired
    private PF2eService pf2eService;

    private String url;

    @When("I request information about all spells by using a GET url")
    public void getAllSpells(){
        url = "https://api.pathfinder2.fr/v1/pf2/spell";
    }

    @Then("the response will return http status ok and data for all spells")
    public void verifyAllSpells(){
        SpellResponse response =  pf2eService.getSpells().block();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getResults());
        Assertions.assertNotNull(response.getCount());
        Assertions.assertNotEquals(0, response.getCount());
        Assertions.assertEquals(response.getCount(),response.getResults().size());
    }
}
