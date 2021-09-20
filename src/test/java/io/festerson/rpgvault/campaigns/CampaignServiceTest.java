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


public class CampaignServiceTest {

    @InjectMocks
    CampaignService campaignService;

    @Mock
    CampaignRepository campaignRepository;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
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
    public void testGetCampaignsByPlayerIdHappyPath() throws Exception {
        Flux<Campaign> campaigns = TestUtils.buildCampaignRepositoryTestCollection();
        List<Campaign> list = campaigns.collectList().block();
        Mockito.when(campaignRepository.getCampaignsByPlayerId("1")).thenReturn(campaigns);
        StepVerifier.create(campaignService.getCampaigns())
            .expectNextCount(list.size());
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
    public void testDeleteCampaignHappyPath() throws Exception {
        Campaign campaign = TestUtils.buildCampaign();
        Mockito.when(campaignRepository.findById("1")).thenReturn(Mono.just(campaign));
        Mockito.when(campaignRepository.delete(campaign)).thenReturn(Mono.empty());
        StepVerifier.create(campaignService.deleteCampaign("1")).verifyComplete();
    }
}
