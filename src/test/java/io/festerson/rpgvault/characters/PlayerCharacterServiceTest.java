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


public class PlayerCharacterServiceTest {

    @InjectMocks
    CharacterService characterService;

    @Mock
    CharacterRepository characterRepository;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCharactersHappyPath() throws Exception {
        Flux<PlayerCharacter> characters = TestUtils.buildCharacterRepositoryTestCollection();
        List<PlayerCharacter> list = characters.collectList().block();
        Mockito.when(characterRepository.findAll()).thenReturn(characters);
        StepVerifier.create(characterService.getCharacters())
            .expectNextCount(list.size())
            .verifyComplete();
    }

    @Test
    public void testGetCharacterByIdHappyPath() throws Exception {
        Mono<PlayerCharacter> character = Mono.just(TestUtils.buildCharacter());
        PlayerCharacter expected = character.block();
        Mockito.when(characterRepository.findById("1")).thenReturn(character);
        StepVerifier.create(characterService.getCharacterById("1"))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getName()).isEqualTo(expected.getName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetCharactersByPlayerIdHappyPath() throws Exception {
        Flux<PlayerCharacter> characters = TestUtils.buildCharacterRepositoryTestCollection();
        List<PlayerCharacter> list = characters.collectList().block();
        Mockito.when(characterRepository.getCharactersByPlayerId("1")).thenReturn(characters);
        StepVerifier.create(characterService.getCharacters())
            .expectNextCount(list.size());
    }

    @Test
    public void testCreateCharacterHappyPath() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        Mockito.when(characterRepository.save(expected)).thenReturn(Mono.just(expected));
        StepVerifier.create(characterService.createCharacter(expected))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getName()).isEqualTo(expected.getName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testUpdateCharacterHappyPath() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        Mockito.when(characterRepository.save(expected)).thenReturn(Mono.just(expected));
        Mockito.when(characterRepository.findById("1")).thenReturn(Mono.just(expected));
        StepVerifier.create(characterService.updateCharacter("1", expected))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getName()).isEqualTo(expected.getName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testDeleteCharacterHappyPath() throws Exception {
        PlayerCharacter character = TestUtils.buildCharacter();
        Mockito.when(characterRepository.findById("1")).thenReturn(Mono.just(character));
        Mockito.when(characterRepository.delete(character)).thenReturn(Mono.empty());
        StepVerifier.create(characterService.deleteCharacter("1")).verifyComplete();
    }
}
