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

public class PlayerControllerTest {

    @InjectMocks
    PlayerController playerController;

    @Mock
    PlayerService playerService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPlayersHappyPath() throws Exception {
        Flux<Player> playerFlux = TestUtils.buildPlayers();
        List<Player> list = playerFlux.collectList().block();
        Mockito.when(playerService.getPlayers()).thenReturn(playerFlux);
        StepVerifier.create(playerController.getPlayers())
            .expectNextMatches(response -> response.getBody().size() == list.size())
            .verifyComplete();
    }

    @Test
    public void testGetPlayerByIdHappyPath() throws Exception {
        Mono<Player> playerMono = Mono.just(TestUtils.buildPlayer());
        Mockito.when(playerService.getPlayerById("1")).thenReturn(playerMono);
        Player expected = playerMono.block();
        StepVerifier.create(playerController.getPlayer("1"))
            .expectNextMatches(response -> {
                Player result = response.getBody();
                Assertions.assertThat(result.getFirstName()).isEqualTo(expected.getFirstName());
                return true;
            })
            .verifyComplete();
    }

   @Test
    public void testCreatePlayerHappyPath() throws Exception {
       Player expected = TestUtils.buildPlayer();
       Mockito.when(playerService.createPlayer(expected)).thenReturn(Mono.just(expected));
       StepVerifier.create(playerController.savePlayer(expected))
           .expectNextMatches(response -> {
               Player result = response.getBody();
               Assertions.assertThat(result.getFirstName()).isEqualTo(expected.getFirstName());
               return true;
           })
           .verifyComplete();
    }


    @Test
    public void testUpdatePlayerHappyPath() throws Exception {
        Player expected = TestUtils.buildPlayer();
        Mockito.when(playerService.updatePlayer("1", expected)).thenReturn(Mono.just(expected));
        StepVerifier.create(playerController.updatePlayer(expected, "1"))
            .expectNextMatches(response -> {
                Player result = response.getBody();
                Assertions.assertThat(result.getFirstName()).isEqualTo(expected.getFirstName());
                return true;
            })
            .verifyComplete();

    }

    @Test
    public void testDeletePlayerHappyPath() throws Exception {
        Mockito.when(playerService.deletePlayer("1")).thenReturn(Mono.empty());
        StepVerifier.create(playerController.deletePlayer("1")).expectNextCount(1).verifyComplete();
    }

}
