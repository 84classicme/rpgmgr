package io.festerson.rpgvault.campaigns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.mongodb.MongoClientException;
import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.exception.ClientException;
import io.festerson.rpgvault.exception.ExceptionEvent;
import io.festerson.rpgvault.exception.ExceptionService;
import io.festerson.rpgvault.exception.RpgMgrException;
import io.festerson.rpgvault.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.mockito.ArgumentMatchers.any;

@WireMockTest(httpPort = 8080)
public class CampaignServiceTest {

    @InjectMocks
    CampaignService campaignService;

    @Mock
    ExceptionService exceptionService;

    @Mock
    CampaignRepository campaignRepository;

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
    public void testGetCampaignsHappyPath() throws Exception {
        Flux<Campaign> campaigns = TestUtils.buildCampaignRepositoryTestCollection();
        List<Campaign> list = campaigns.collectList().block();
        Mockito.when(campaignRepository.findAll()).thenReturn(campaigns);
        StepVerifier.create(campaignService.getCampaigns())
            .expectNextCount(list.size())
            .verifyComplete();
    }

    @Test
    public void testGetPlayersException() throws Exception {
        String message = "TEST FAIL";
        Mockito.doReturn(Flux.error(new MongoClientException(message))).when(campaignRepository).findAll();
        StepVerifier.create(campaignService.getCampaigns()).verifyErrorMatches(throwable -> {
            Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
            Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
            Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
            return true;
        });
    }

    @Test
    public void testGetCampaignByIdHappyPath() throws Exception {
        Mono<Campaign> campaign = Mono.just(TestUtils.buildCampaign());
        Campaign expected = campaign.block();
        Mockito.when(campaignRepository.findById("1")).thenReturn(campaign);
        StepVerifier.create(campaignService.getCampaignById("1"))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getDescription()).isEqualTo(expected.getDescription());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetCampaignByIdException() throws Exception {
        String message = "TEST FAIL";
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(campaignRepository).findById("1");
        StepVerifier.create(campaignService.getCampaignById("1")).verifyErrorMatches(throwable -> {
            Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
            Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
            Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
            return true;
        });
    }

    @Test
    public void testGetCampaignsByPlayerIdHappyPath() throws Exception {
        Flux<Campaign> campaigns = TestUtils.buildCampaignRepositoryTestCollection();
        List<Campaign> list = campaigns.collectList().block();
        Mockito.when(campaignRepository.getCampaignsByPlayerId("1")).thenReturn(campaigns);
        StepVerifier.create(campaignService.getCampaignsByPlayerId("1"))
            .expectNextCount(list.size());
    }

    @Test
    public void testGetCampaignsByPlayerIdException() throws Exception {
        String message = "TEST FAIL";
        Mockito.doReturn(Flux.error(new MongoClientException(message))).when(campaignRepository).getCampaignsByPlayerId("1");
        StepVerifier.create(campaignService.getCampaignsByPlayerId("1"))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
            return true;
        });
    }

    @Test
    public void testCreateCampaignHappyPath() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        Mockito.when(campaignRepository.save(expected)).thenReturn(Mono.just(expected));
        StepVerifier.create(campaignService.createCampaign(expected))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getDescription()).isEqualTo(expected.getDescription());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testCreateCampaignException() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        String message = "TEST FAIL";
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(campaignRepository).save(expected);
        StepVerifier.create(campaignService.createCampaign(expected))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
    }

    @Test
    public void testUpdateCampaignHappyPath() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        Mockito.when(campaignRepository.save(expected)).thenReturn(Mono.just(expected));
        Mockito.when(campaignRepository.findById("1")).thenReturn(Mono.just(expected));
        StepVerifier.create(campaignService.updateCampaign("1", expected))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getDescription()).isEqualTo(expected.getDescription());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testUpdateCampaignException() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        String message = "TEST FAIL";
        Mockito.when(campaignRepository.findById("1")).thenReturn(Mono.just(expected));
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(campaignRepository).save(expected);
        StepVerifier.create(campaignService.updateCampaign("1", expected))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
    }

    @Test
    public void testDeleteCampaignHappyPath() throws Exception {
        Campaign campaign = TestUtils.buildCampaign();
        Mockito.when(campaignRepository.findById("1")).thenReturn(Mono.just(campaign));
        Mockito.when(campaignRepository.delete(campaign)).thenReturn(Mono.empty());
        StepVerifier.create(campaignService.deleteCampaign("1")).verifyComplete();
    }

    @Test
    public void testDeleteCampaignException() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        String message = "TEST FAIL";
        Mockito.when(campaignRepository.findById("1")).thenReturn(Mono.just(expected));
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(campaignRepository).delete(expected);
        StepVerifier.create(campaignService.deleteCampaign("1"))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
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

    @Test
    public void testConnectionResetByPeerRest() throws IOException {
        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));
        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
        Mockito.when(exceptionService.recordExceptionEvent(any(ExceptionEvent.class))).thenReturn(Mono.empty());

        StepVerifier.create(campaignService.getCampaignDataRestService(request))
            .expectErrorMatches(this::checkRetryTimeoutMessage)
            .verify();
    }

    @Test
    public void testEmptyResponseRest() throws IOException {
        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));
        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
        Mockito.when(exceptionService.recordExceptionEvent(any(ExceptionEvent.class))).thenReturn(Mono.empty());

        StepVerifier.create(campaignService.getCampaignDataRestService(request))
            .expectErrorMatches(this::checkRetryTimeoutMessage)
            .verify();
    }

    @Test
    public void testMalformedResponseChunkRest() throws IOException {
        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
        Mockito.when(exceptionService.recordExceptionEvent(any(ExceptionEvent.class))).thenReturn(Mono.empty());

        StepVerifier.create(campaignService.getCampaignDataRestService(request))
            .expectErrorMatches(this::checkRetryTimeoutMessage)
            .verify();
    }

    @Test
    public void testRandomDataRest() throws IOException {
        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT)).willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
        CampaignRequest request = TestUtils.getCampaignRequestFromJson("src/test/resources/json/campaign.json");
        Mockito.when(exceptionService.recordExceptionEvent(any())).thenReturn(Mono.empty());

        StepVerifier.create(campaignService.getCampaignDataRestService(request))
            .expectErrorMatches(this::checkRetryTimeoutMessage)
            .verify();
    }

    private void mockGetCountryRest(final String pathToJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CampaignRequest response = mapper.readValue(new File(pathToJson), CampaignRequest.class);
        stubFor(get(urlMatching(REST_SERVICE_ENDPOINT))
            .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(response))));
    }

    private boolean checkRetryTimeoutMessage(Throwable t){
        return t.getMessage().equals(MAX_RETRY_MESSAGE);
    }
}
