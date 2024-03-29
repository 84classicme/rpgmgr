package io.festoso.rpgvault.characters;

import com.mongodb.MongoClientException;
import io.festoso.rpgvault.domain.PlayerCharacter;
import io.festoso.rpgvault.exception.RpgMgrException;
import io.festoso.rpgvault.util.TestUtils;
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
        MockitoAnnotations.openMocks(this);
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
    public void testGetCharactersException() throws Exception {
        String message = "TEST FAIL";
        Mockito.doReturn(Flux.error(new MongoClientException(message))).when(characterRepository).findAll();
        StepVerifier.create(characterService.getCharacters())
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
            return true;
        });
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
    public void testGetCharacterByIdException() throws Exception {
        String message = "TEST FAIL";
        Mono<PlayerCharacter> character = Mono.just(TestUtils.buildCharacter());
        PlayerCharacter expected = character.block();
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(characterRepository).findById("1");
        StepVerifier.create(characterService.getCharacterById("1"))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
            return true;
        });
    }

    @Test
    public void testGetCharactersByPlayerIdHappyPath() throws Exception {
        Flux<PlayerCharacter> characters = TestUtils.buildCharacterRepositoryTestCollection();
        List<PlayerCharacter> list = characters.collectList().block();
        Mockito.when(characterRepository.getCharactersByPlayerId("1")).thenReturn(characters);
        StepVerifier.create(characterService.getCharactersByPlayerId("1"))
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
    public void testCreateCharacterException() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        String message = "TEST FAIL";
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(characterRepository).save(expected);
        StepVerifier.create(characterService.createCharacter(expected))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
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
    public void testUpdateCampaignException() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        String message = "TEST FAIL";
        Mockito.when(characterRepository.findById("1")).thenReturn(Mono.just(expected));
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(characterRepository).save(expected);
        StepVerifier.create(characterService.updateCharacter("1", expected))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
    }

    @Test
    public void testDeleteCharacterHappyPath() throws Exception {
        PlayerCharacter character = TestUtils.buildCharacter();
        Mockito.when(characterRepository.findById("1")).thenReturn(Mono.just(character));
        Mockito.when(characterRepository.delete(character)).thenReturn(Mono.empty());
        StepVerifier.create(characterService.deleteCharacter("1")).verifyComplete();
    }

    @Test
    public void testDeleteCampaignException() throws Exception {
        PlayerCharacter expected = TestUtils.buildCharacter();
        String message = "TEST FAIL";
        Mockito.when(characterRepository.findById("1")).thenReturn(Mono.just(expected));
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(characterRepository).delete(expected);
        StepVerifier.create(characterService.deleteCharacter("1"))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
    }
}
