package io.festerson.rpgvault.players;

import io.festerson.rpgvault.domain.Player;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static io.festerson.rpgvault.MdcConfig.logOnNext;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@Slf4j
@RequestMapping(value = "/players")
@SecurityRequirement(name = "jwt")
public class PlayerController {

    private final PlayerService playerService;


    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<List<Player>>> getPlayers(Principal principal) {
        //log.info("Getting data for all players.");
        String user = principal !=null && principal.getName() != null ? principal.getName() : "";
        return playerService.getPlayers()
            .collectList()
            .doOnEach(logOnNext(list -> log.info("found {} players", list != null ? list.size() : 0)))
            .map(allFound -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(allFound))
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching all players")
                .build())
            .contextWrite(Context.of("USER", user));
    }

    @GetMapping(value="/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Player>> getPlayer(Principal principal, @PathVariable String playerId) {
        log.info("Getting data for player id: " + playerId);
        return playerService.getPlayerById(playerId)
            .map(found -> ResponseEntity.ok().body(found))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching player id: " + playerId)
                .build())
            .contextWrite(Context.of("USER",  principal.getName()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Player>> createPlayer(Principal principal, @Valid @RequestBody Player player) {
        log.info("Creating new player: " + player);
        return playerService.createPlayer(player)
            .map(saved -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(saved))
            .defaultIfEmpty(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("RpgMgrMessage", "Server error creating player: " + player)
                    .build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error creating player.")
                .build())
            .contextWrite(Context.of("USER",  principal.getName()));
    }

    @PutMapping(value="/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Player>> updatePlayer(Principal principal, @Valid @RequestBody Player player, @PathVariable String playerId){
        log.info("Updating player with data: " + player);
        return playerService.updatePlayer(playerId, player)
            .map(updatedCharacter -> ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedCharacter))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error creating player id: " + playerId)
                .build())
            .contextWrite(Context.of("USER",  principal.getName()));
    }

    @DeleteMapping(value="/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePlayer(Principal principal, @PathVariable String playerId){
        log.info("Deleting player: " + playerId);
        return playerService.deletePlayer(playerId)
            .thenReturn(ResponseEntity.noContent().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error deleting player id: " + playerId)
                .build())
            .contextWrite(Context.of("USER",  principal.getName()));
    }
}
