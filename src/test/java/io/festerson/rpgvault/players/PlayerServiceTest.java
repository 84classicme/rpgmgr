package io.festerson.rpgvault.players;

import com.mongodb.MongoClientException;
import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.exception.RpgMgrException;
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


public class PlayerServiceTest {

    @InjectMocks
    PlayerService playerService;

    @Mock
    PlayerRepository playerRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPlayersHappyPath() throws Exception {
        Flux<Player> playerFlux = TestUtils.buildPlayers();
        List<Player> list = playerFlux.collectList().block();
        Mockito.when(playerRepository.findAll()).thenReturn(playerFlux);
        StepVerifier.create(playerService.getPlayers())
            .expectNextCount(list.size())
            .verifyComplete();
    }

    @Test
    public void testGetPlayersException() throws Exception {
        String message = "TEST FAIL";
        Mockito.doReturn(Flux.error(new MongoClientException(message))).when(playerRepository).findAll();
        StepVerifier.create(playerService.getPlayers()).verifyErrorMatches(throwable -> {
            Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
            Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
            Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
            return true;
        });
    }

    @Test
    public void testGetPlayerByIdHappyPath() throws Exception {
        Mono<Player> player = Mono.just(TestUtils.buildPlayer());
        Player expected = player.block();
        Mockito.when(playerRepository.findById("1")).thenReturn(player);
        StepVerifier.create(playerService.getPlayerById("1"))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getFirstName()).isEqualTo(expected.getFirstName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetPlayerByIdException() throws Exception {
        String message = "TEST FAIL";
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(playerRepository).findById("1");
        StepVerifier.create(playerService.getPlayerById("1"))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
        });
    }


    @Test
    public void testCreatePlayerHappyPath() throws Exception {
        Player expected = TestUtils.buildPlayer();
        Mockito.when(playerRepository.save(expected)).thenReturn(Mono.just(expected));
        StepVerifier.create(playerService.createPlayer(expected))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getFirstName()).isEqualTo(expected.getFirstName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testCreatePlayerException() throws Exception {
        Player expected = TestUtils.buildPlayer();
        String message = "TEST FAIL";
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(playerRepository).save(expected);
        StepVerifier.create(playerService.createPlayer(expected))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
    }

    @Test
    public void testUpdatePlayerHappyPath() throws Exception {
        Player expected = TestUtils.buildPlayer();
        Mockito.when(playerRepository.save(expected)).thenReturn(Mono.just(expected));
        Mockito.when(playerRepository.findById("1")).thenReturn(Mono.just(expected));
        StepVerifier.create(playerService.updatePlayer("1", expected))
            .expectNextMatches(result -> {
                Assertions.assertThat(result.getFirstName()).isEqualTo(expected.getFirstName());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testUpdatePlayerException() throws Exception {
        Player expected = TestUtils.buildPlayer();
        String message = "TEST FAIL";
        Mockito.when(playerRepository.findById("1")).thenReturn(Mono.just(expected));
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(playerRepository).save(expected);
        StepVerifier.create(playerService.updatePlayer("1", expected))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
    }

    @Test
    public void testDeletePlayerHappyPath() throws Exception {
        Player expected = TestUtils.buildPlayer();
        Mockito.when(playerRepository.findById("1")).thenReturn(Mono.just(expected));
        Mockito.when(playerRepository.delete(expected)).thenReturn(Mono.empty());
        StepVerifier.create(playerService.deletePlayer("1")).verifyComplete();
    }

    @Test
    public void testDeletePlayerException() throws Exception {
        Player expected = TestUtils.buildPlayer();
        String message = "TEST FAIL";
        Mockito.when(playerRepository.findById("1")).thenReturn(Mono.just(expected));
        Mockito.doReturn(Mono.error(new MongoClientException(message))).when(playerRepository).delete(expected);
        StepVerifier.create(playerService.deletePlayer("1"))
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable instanceof RpgMgrException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed() instanceof MongoClientException).isTrue();
                Assertions.assertThat(((RpgMgrException) throwable).getComposed().getMessage()).isEqualTo(message);
                return true;
            });
    }
}
