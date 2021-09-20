package io.festerson.rpgvault.campaigns;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
    public void testDeleteCampaignHappyPath() throws Exception {
        Mockito.when(campaignService.deleteCampaign("1")).thenReturn(Mono.empty());
        StepVerifier.create(campaignController.deleteCampaign("1")).expectNextCount(1).verifyComplete();
    }
}
