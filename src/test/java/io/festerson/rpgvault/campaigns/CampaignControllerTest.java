package io.festerson.rpgvault.campaigns;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.exception.RpgMgrException;
import io.festerson.rpgvault.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;


public class CampaignControllerTest {

    @InjectMocks
    CampaignController campaignController;

    @Mock
    CampaignService campaignService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCampaignsByNullPlayerId() throws Exception {
        Flux<Campaign> campaigns = TestUtils.buildCampaignRepositoryTestCollection();
        List<Campaign> list = campaigns.collectList().block();
        Mockito.when(campaignService.getCampaigns()).thenReturn(campaigns);
        StepVerifier.create(campaignController.getCampaigns(null))
            .expectNextMatches(response -> response.getBody().size() == list.size())
            .verifyComplete();
    }

    @Test
    public void testGetAllCampaignsByEmptyPlayerId() throws Exception {
        Flux<Campaign> campaigns = TestUtils.buildCampaignRepositoryTestCollection();
        List<Campaign> list = campaigns.collectList().block();
        Mockito.when(campaignService.getCampaigns()).thenReturn(campaigns);
        StepVerifier.create(campaignController.getCampaigns(""))
            .expectNextMatches(response -> response.getBody().size() == list.size())
            .verifyComplete();
    }

    @Test
    public void testGetCampaignsByPlayerIdHappyPath() throws Exception {
        Flux<Campaign> campaigns = TestUtils.buildCampaignRepositoryTestCollection();
        List<Campaign> list = campaigns.collectList().block();
        Mockito.when(campaignService.getCampaignsByPlayerId(any())).thenReturn(campaigns);
        StepVerifier.create(campaignController.getCampaigns("1"))
            .expectNextMatches(response -> response.getBody().size() == list.size())
            .verifyComplete();
    }

    @Test
    public void testGetCampaignsByPlayerIdException() throws Exception {
        Mockito.doReturn(Flux.error(new RpgMgrException())).when(campaignService).getCampaignsByPlayerId(any());
        StepVerifier.create(campaignController.getCampaigns("1"))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error fetching all campaigns");
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetCampaignByIdHappyPath() throws Exception {
        Mono<Campaign> campaign = Mono.just(TestUtils.buildCampaign());
        Campaign expected = campaign.block();
        Mockito.when(campaignService.getCampaignById("1")).thenReturn(campaign);
        StepVerifier.create(campaignController.getCampaign("1"))
            .expectNextMatches(response -> {
                Campaign result = response.getBody();
                Assertions.assertThat(result.getDescription()).isEqualTo(expected.getDescription());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetCampaignByIdException() throws Exception {
        Mockito.doReturn(Mono.error(new RpgMgrException())).when(campaignService).getCampaignById(any());
        StepVerifier.create(campaignController.getCampaign("1"))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error fetching campaign id: 1");
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testCreateCampaignHappyPath() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        Mockito.when(campaignService.createCampaign(any())).thenReturn(Mono.just(expected));
        StepVerifier.create(campaignController.saveCampaign(expected))
            .expectNextMatches(response -> {
                Campaign result = response.getBody();
                Assertions.assertThat(result.getDescription()).isEqualTo(expected.getDescription());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testCreateCampaignException() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        Mockito.doReturn(Mono.error(new RpgMgrException())).when(campaignService).createCampaign(any());
        StepVerifier.create(campaignController.saveCampaign(expected))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error creating campaign: " + expected);
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testUpdateCampaignHappyPath() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        Mockito.when(campaignService.updateCampaign("1", expected)).thenReturn(Mono.just(expected));
        StepVerifier.create(campaignController.updateCampaign(expected, "1"))
            .expectNextMatches(response -> {
                Campaign result = response.getBody();
                Assertions.assertThat(result.getDescription()).isEqualTo(expected.getDescription());
                return true;
            })
            .verifyComplete();
    }


    @Test
    public void testUpdateCampaignException() throws Exception {
        Campaign expected = TestUtils.buildCampaign();
        Mockito.doReturn(Mono.error(new RpgMgrException())).when(campaignService).updateCampaign("1", expected);
        StepVerifier.create(campaignController.updateCampaign(expected, "1"))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error updating campaign: " + expected);
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testDeleteCampaignHappyPath() throws Exception {
        Mockito.when(campaignService.deleteCampaign("1")).thenReturn(Mono.empty());
        StepVerifier.create(campaignController.deleteCampaign("1")).expectNextCount(1).verifyComplete();
    }

    @Test
    public void testDeleteCampaignException() throws Exception {
        Mockito.doReturn(Mono.error(new RpgMgrException())).when(campaignService).deleteCampaign("1");
        StepVerifier.create(campaignController.deleteCampaign("1"))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error deleting character id :1");
                return true;
            })
            .verifyComplete();
    }
}
