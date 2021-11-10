package io.festerson.rpgvault.campaigns;

import com.mongodb.MongoClientException;
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
import org.springframework.test.util.ReflectionTestUtils;
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
    public void testGetCampaignsException() throws Exception {
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
}
