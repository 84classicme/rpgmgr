package io.festerson.rpgvault.characters;

import io.festerson.rpgvault.domain.PlayerCharacter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@CommonsLog
@RestController
public class CharacterController {

    private CharacterService characterService;

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public CharacterController(CharacterService characterRepository) {
        this.characterService = characterRepository;
    }

    // Is there way to duplicate how the handler does this?
    // Controller do not have access to ServerRequest.
    @GetMapping("/characters")
    public Mono<ResponseEntity<List<PlayerCharacter>>> getCharacters(@RequestParam(value="player", required=false) String playerId) {
        if (playerId == null || playerId.isEmpty()) {
            return characterService.getCharacters()
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
                .onErrorReturn(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("RpgMgrMessage", "Server error fetching all campaigns")
                    .build());
        }
        return characterService.getCharactersByPlayerId(playerId)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching characters for player id: " + playerId)
                .build());

    }

    @GetMapping("/characters/{characterId}")
    public Mono<ResponseEntity<PlayerCharacter>> getCharacter(@PathVariable String characterId) {
        return characterService.getCharacterById(characterId)
            .map(character -> ResponseEntity.ok().body(character))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching character id: " + characterId)
                .build());
    }

    @PostMapping("/characters")
    public Mono<ResponseEntity<PlayerCharacter>> saveCharacter(@Valid @RequestBody PlayerCharacter playerCharacter) {
        return characterService.createCharacter(playerCharacter)
            .map(saved -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(saved))
            .defaultIfEmpty(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error creating character: " + playerCharacter)
                .build());
    }

    @PutMapping("/characters/{characterId}")
    public Mono<ResponseEntity<PlayerCharacter>> updateCharacter(@Valid @RequestBody PlayerCharacter playerCharacter, @PathVariable String characterId){
        return characterService.updateCharacter(characterId, playerCharacter)
            .map(updatedCharacter -> ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedCharacter))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error updating character: " + playerCharacter)
                .build());
    }

    @DeleteMapping("/characters/{characterId}")
    public Mono<ResponseEntity<Void>> deleteCharacter(@PathVariable String characterId){
        return characterService.deleteCharacter(characterId)
            .thenReturn(ResponseEntity.noContent().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error deleting character id: " + characterId)
                .build());
    }
}
