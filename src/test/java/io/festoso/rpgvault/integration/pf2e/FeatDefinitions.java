package io.festoso.rpgvault.integration.pf2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.festoso.rpgvault.domain.pf2api.FeatResponse;
import io.festoso.rpgvault.integration.CucumberTest;
import io.festoso.rpgvault.pf2e.PF2eService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class FeatDefinitions extends CucumberTest {

    @Autowired
    private PF2eService pf2eService;

    private static final String WIREMOCK_PORT = PF2APIMockServer.WIREMOCK_PORT;
    private static final WireMockServer wireMockServer = PF2APIMockServer.setupWireMock();
    private static final String ENDPOINT_VARIABLE_NAME = "featEndpointUrl";
    private static final String ENDPOINT_PATH = "/v1/pf2/feat";
    private static final String ENDPOINT_VARIABLE_VALUE = "http://localhost:" + WIREMOCK_PORT + ENDPOINT_PATH;
    private static final String PATH_TO_EXPECTED_FILE = "src/test/resources/json/pf2e-api-feat-response.json";

    @Before
    public static void setupWireMock() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
            WireMock.configureFor("localhost", Integer.parseInt(WIREMOCK_PORT));
        }
    }

    @After
    public static void tearDownWireMock() {
        wireMockServer.stop();
    }

    @When("I request information about all feats by using a GET url")
    public void getAllFeats(){
        ReflectionTestUtils.setField(pf2eService, ENDPOINT_VARIABLE_NAME, ENDPOINT_VARIABLE_VALUE);
    }

    @Then("the response will return http status ok and data for all feats")
    public void verifyAllFeats() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FeatResponse expected = mapper.readValue(new File(PATH_TO_EXPECTED_FILE), FeatResponse.class);
        stubFor(get(ENDPOINT_PATH)
            .willReturn(status(200)
                .withHeader("Content-Type", "application/json")
                .withBody(mapper.writeValueAsString(expected))));
        FeatResponse response = pf2eService.getFeats().block();
        Assertions.assertNotNull(response.getResults());
        Assertions.assertNotNull(response.getCount());
        Assertions.assertNotEquals(0, response.getCount());
        Assertions.assertEquals(response.getCount(),response.getResults().size());
        Assertions.assertEquals(response.getCount(),expected.getResults().size());
    }
}
