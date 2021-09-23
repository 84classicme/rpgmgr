package io.festerson.rpgvault.players;

import io.festerson.rpgvault.domain.Player;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@CommonsLog
@RestController
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value="/players", method = RequestMethod.GET)
    public Mono<ResponseEntity<List<Player>>> getPlayers() {
        log.info("Getting data for all players.");
        return playerService.getPlayers()
            .collectList()
            .map(allFound -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(allFound))
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching all players")
                .build());
    }

    @RequestMapping(value="/players/{playerId}", method = RequestMethod.GET)
    public Mono<ResponseEntity<Player>> getPlayer(@PathVariable String playerId) {
        log.info("Getting data for player id: " + playerId);
        return playerService.getPlayerById(playerId)
            .map(found -> ResponseEntity.ok().body(found))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching player id :" + playerId)
                .build());
    }

    @RequestMapping(value="/players", method = RequestMethod.POST)
    public Mono<ResponseEntity<Player>> createPlayer(@Valid @RequestBody Player player) {
        log.info("Creating new player: " + player);
        return playerService.createPlayer(player)
            .map(saved -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(saved))
            .defaultIfEmpty(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("RpgMgrMessage", "Server error creating player.")
                    .build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error creating player.")
                .build());
    }

    @RequestMapping(value="/players/{playerId}", method = RequestMethod.PUT)
    public Mono<ResponseEntity<Player>> updatePlayer(@Valid @RequestBody Player player, @PathVariable String playerId){
        log.info("Updating player with data: " + player);
        return playerService.updatePlayer(playerId, player)
            .map(updatedCharacter -> ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedCharacter))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error creating player id:" + playerId)
                .build());
    }

    @RequestMapping(value="/players/{playerId}", method = RequestMethod.DELETE)
    public Mono<ResponseEntity<Void>> deletePlayer(@PathVariable String playerId){
        log.info("Deleting player: " + playerId);
        return playerService.deletePlayer(playerId)
            .thenReturn(ResponseEntity.noContent().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error deleting player id :" + playerId)
                .build());
    }
}
