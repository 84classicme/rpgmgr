package io.festerson.rpgvault.integration.pf2e;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.festerson.rpgvault.domain.pf2api.ClassResponse;
import io.festerson.rpgvault.integration.CucumberTest;
import io.festerson.rpgvault.pf2e.PF2eService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

public class ClassDefinitions extends CucumberTest {

    @Autowired
    private PF2eService pf2eService;

    private String url;

    @When("I request information about all classes by using a GET url")
    public void getAllClasses(){
        url = "https://api.pathfinder2.fr/v1/pf2/class";
    }

    @Then("the response will return http status ok and data for all classes")
    public void verifyAllClasses(){
        ClassResponse response = pf2eService.getClasses().block();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getResults());
        Assertions.assertNotNull(response.getCount());
        Assertions.assertNotEquals(0, response.getCount());
        Assertions.assertEquals(response.getCount(),response.getResults().size());
    }
}
