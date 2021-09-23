package io.festerson.rpgvault.characters;

import io.festerson.rpgvault.domain.PlayerCharacter;
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

public class PlayerCharacterControllerTest {

    @InjectMocks
    CharacterController characterController;

    @Mock
    CharacterService characterService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCharactersByNullPlayerId() throws Exception {
        Flux<PlayerCharacter> campaigns = TestUtils.buildCharacterRepositoryTestCollection();
        List<PlayerCharacter> list = campaigns.collectList().block();
        Mockito.when(characterService.getCharacters()).thenReturn(campaigns);
        StepVerifier.create(characterController.getCharacters(null))
            .expectNextMatches(response -> response.getBody().size() == list.size())
            .verifyComplete();
    }

    @Test
    public void testGetAllCharactersByEmptyPlayerId() throws Exception {
        Flux<PlayerCharacter> characterFlux = TestUtils.buildCharacterRepositoryTestCollection();
        List<PlayerCharacter> list = characterFlux.collectList().block();
        Mockito.when(characterService.getCharacters()).thenReturn(characterFlux);
        StepVerifier.create(characterController.getCharacters(""))
            .expectNextMatches(response -> response.getBody().size() == list.size())
            .verifyComplete();
    }

    @Test
    public void testGetCampaignsByPlayerIdHappyPath() throws Exception {
        Flux<PlayerCharacter> characterFlux = TestUtils.buildCharacterRepositoryTestCollection();
        List<PlayerCharacter> list = characterFlux.collectList().block();
        Mockito.when(characterService.getCharactersByPlayerId(any())).thenReturn(characterFlux);
        StepVerifier.create(characterController.getCharacters("1"))
            .expectNextMatches(response -> response.getBody().size() == list.size())
            .verifyComplete();
    }

    @Test
    public void testGetAllCharactersByPlayerIdException() throws Exception {
        Mockito.doReturn(Flux.error(new RpgMgrException())).when(characterService).getCharactersByPlayerId("1");
        StepVerifier.create(characterController.getCharacters("1"))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error fetching campaign id: 1");
                return true;
            })
            .verifyComplete();
    }


    @Test
    public void testGetCharacterByIdHappyPath() throws Exception {
        Mono<PlayerCharacter> campaign = Mono.just(TestUtils.buildCharacter());
        PlayerCharacter expected = campaign.block();
        Mockito.when(characterService.getCharacterById("1")).thenReturn(campaign);
        StepVerifier.create(characterController.getCharacter("1"))
            .expectNextMatches(response -> {
                PlayerCharacter result = response.getBody();
                Assertions.assertThat(result.getName()).isEqualTo(expected.getName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetCharacterByIdException() throws Exception {
        Mockito.doReturn(Mono.error(new RpgMgrException())).when(characterService).getCharacterById(any());
        StepVerifier.create(characterController.getCharacter("1"))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error fetching campaign id: 1");
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testCreateCharacterHappyPath() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        Mockito.when(characterService.createCharacter(expected)).thenReturn(Mono.just(expected));
        StepVerifier.create(characterController.saveCharacter(expected))
            .expectNextMatches(response -> {
                PlayerCharacter result = response.getBody();
                Assertions.assertThat(result.getName()).isEqualTo(expected.getName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testCreateCharacterException() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        Mockito.doReturn(Mono.error(new RpgMgrException())).when(characterService).createCharacter(any());
        StepVerifier.create(characterController.saveCharacter(expected))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error creating campaign: " + expected);
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testUpdateCharacterHappyPath() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        Mockito.when(characterService.updateCharacter("1", expected)).thenReturn(Mono.just(expected));
        StepVerifier.create(characterController.updateCharacter(expected, "1"))
            .expectNextMatches(response -> {
                PlayerCharacter result = response.getBody();
                Assertions.assertThat(result.getName()).isEqualTo(expected.getName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testUpdateCharacterException() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        Mockito.doReturn(Mono.error(new RpgMgrException())).when(characterService).updateCharacter("1", expected);
        StepVerifier.create(characterController.updateCharacter(expected, "1"))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error updating campaign: " + expected);
                return true;
            })
            .verifyComplete();
    }


    @Test
    public void testDeleteCharacterHappyPath() throws Exception {
        Mockito.when(characterService.deleteCharacter("1")).thenReturn(Mono.empty());
        StepVerifier.create(characterController.deleteCharacter("1")).expectNextCount(1).verifyComplete();
    }

    @Test
    public void testDeleteCharacterException() throws Exception {
        Mockito.doReturn(Mono.error(new RpgMgrException())).when(characterService).deleteCharacter("1");
        StepVerifier.create(characterController.deleteCharacter("1"))
            .expectNextMatches(response -> {
                response.getStatusCode().is5xxServerError();
                HttpHeaders headers = response.getHeaders();
                headers.get("RpgMgrMessage").equals("Server error deleting character id :1");
                return true;
            })
            .verifyComplete();
    }
}


