package io.festerson.rpgvault.integration.pf2e;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.festerson.rpgvault.domain.pf2api.BackgroundResponse;
import io.festerson.rpgvault.integration.CucumberTest;
import io.festerson.rpgvault.pf2e.PF2eService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

public class BackgroundDefinitions extends CucumberTest {

    @Autowired
    private PF2eService pf2eService;

    private String url;

    @When("I request information about all backgrounds by using a GET url")
    public void getAllBackgrounds(){
        url = "https://api.pathfinder2.fr/v1/pf2/background";
    }

    @Then("the response will return http status ok and data for all backgrounds")
    public void verifyAllBackgrounds(){
        BackgroundResponse response = pf2eService.getBackgrounds().block();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getResults());
        Assertions.assertNotNull(response.getCount());
        Assertions.assertNotEquals(0, response.getCount());
        Assertions.assertEquals(response.getCount(),response.getResults().size());
    }
}
