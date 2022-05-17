package io.festoso.rpgvault;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.festoso.rpgvault.campaigns.CampaignRequest;
import io.festoso.rpgvault.campaigns.CampaignService;
import io.festoso.rpgvault.exception.ClientException;
import io.festoso.rpgvault.exception.ExceptionEvent;
import io.festoso.rpgvault.exception.ExceptionService;
import io.festoso.rpgvault.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.ArgumentMatchers.any;

@WireMockTest(httpPort = 8080)
public class WebClientConfigTest {

    @InjectMocks
    CampaignService campaignService;

    @Mock
    ExceptionService exceptionService;

    @Captor
    ArgumentCaptor<ExceptionEvent> exceptionEventCaptor;

    private static final String MAX_RETRY_MESSAGE = "External Service failed to process after max retries.";
    private final String REST_SERVICE_ENDPOINT = "/campaigns";

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(campaignService, "restEndpointUrl", "http://localhost:8080/campaigns");
    }

    @Test
    public void testGetCountryFromRest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CampaignRequest mockresponse = mapper.readValue(new File("src/test/resources/json/campaign.json"), CampaignRequest.class);
        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT))
            .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(mockresponse))));
        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
        Mockito.when(exceptionService.recordExceptionEvent(any())).thenReturn(Mono.empty());
        StepVerifier.create(campaignService.getCampaignDataRestService(request))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getMonsters()).isNotNull();
                Assertions.assertThat(result.getMonsters().size()).isEqualTo(mockresponse.getMonsters().size());
                Assertions.assertThat(result.getNpcs()).isNotNull();
                Assertions.assertThat(result.getNpcs().size()).isEqualTo(mockresponse.getNpcs().size());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testHandle4xxFromRest() throws IOException {
        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withStatus(400)));
        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
        Mockito.when(exceptionService.recordExceptionEvent(any(ExceptionEvent.class))).thenReturn(Mono.empty());

        StepVerifier.create(campaignService.getCampaignDataRestService(request))
            .expectErrorMatches(t -> t.getMessage().equals("Cannot process CampaignService.getCampaignDataRestService due to client error.") &&
                t.getSuppressed()[0] instanceof ClientException &&
                t.getSuppressed()[0].getMessage().equals("CLIENT EXCEPTION in CampaignService.") &&
                ((ClientException)t.getSuppressed()[0]).getCode() == 400)
            .verifyThenAssertThat()
            .hasNotDroppedElements()
            .hasNotDiscardedElements()
            .hasNotDroppedErrors();

        Mockito.verify(exceptionService).recordExceptionEvent(exceptionEventCaptor.capture());
        ExceptionEvent captured = exceptionEventCaptor.getValue();
        Assertions.assertThat(captured).isNotNull();
    }

    @Test
    public void testHandle5xxFromRest() throws IOException {
        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withStatus(500)));
        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
        Mockito.when(exceptionService.recordExceptionEvent(any(ExceptionEvent.class))).thenReturn(Mono.empty());

        StepVerifier.create(campaignService.getCampaignDataRestService(request))
            .expectErrorMatches(this::checkRetryTimeoutMessage)
            .verify();
    }
//
//    @Test
//    public void testConnectionResetByPeerRest() throws IOException {
//        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));
//        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
//        Mockito.when(exceptionService.recordExceptionEvent(any(ExceptionEvent.class))).thenReturn(Mono.empty());
//
//        StepVerifier.create(campaignService.getCampaignDataRestService(request))
//            .expectErrorMatches(this::checkRetryTimeoutMessage)
//            .verify();
//    }
//
//    @Test
//    public void testEmptyResponseRest() throws IOException {
//        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));
//        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
//        Mockito.when(exceptionService.recordExceptionEvent(any(ExceptionEvent.class))).thenReturn(Mono.empty());
//
//        StepVerifier.create(campaignService.getCampaignDataRestService(request))
//            .expectErrorMatches(this::checkRetryTimeoutMessage)
//            .verify();
//    }
//
//    @Test
//    public void testMalformedResponseChunkRest() throws IOException {
//        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
//        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
//        Mockito.when(exceptionService.recordExceptionEvent(any(ExceptionEvent.class))).thenReturn(Mono.empty());
//
//        StepVerifier.create(campaignService.getCampaignDataRestService(request))
//            .expectErrorMatches(this::checkRetryTimeoutMessage)
//            .verify();
//    }
//
//    @Test
//    public void testRandomDataRest() throws IOException {
//        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
//        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
//        Mockito.when(exceptionService.recordExceptionEvent(any())).thenReturn(Mono.empty());
//
//        StepVerifier.create(campaignService.getCampaignDataRestService(request))
//            .expectErrorMatches(this::checkRetryTimeoutMessage)
//            .verify();
//    }
//
//    private void mockGetCountryRest(final String pathToJson) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        CampaignRequest response = mapper.readValue(new File(pathToJson), CampaignRequest.class);
//        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT))
//            .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(response))));
//    }

    private boolean checkRetryTimeoutMessage(Throwable t){
        return t.getMessage().equals(MAX_RETRY_MESSAGE);
    }
}
