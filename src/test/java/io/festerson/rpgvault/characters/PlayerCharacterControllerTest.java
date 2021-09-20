package io.festerson.rpgvault.characters;

import io.festerson.rpgvault.domain.PlayerCharacter;
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
    public void testDeleteCharacterHappyPath() throws Exception {
        Mockito.when(characterService.deleteCharacter("1")).thenReturn(Mono.empty());
        StepVerifier.create(characterController.deleteCharacter("1")).expectNextCount(1).verifyComplete();
    }
}


