package io.festerson.rpgvault.players;

import io.festerson.rpgvault.domain.Player;
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
    public void testDeletePlayerHappyPath() throws Exception {
        Player expected = TestUtils.buildPlayer();
        Mockito.when(playerRepository.findById("1")).thenReturn(Mono.just(expected));
        Mockito.when(playerRepository.delete(expected)).thenReturn(Mono.empty());
        StepVerifier.create(playerService.deletePlayer("1")).verifyComplete();
    }
}
