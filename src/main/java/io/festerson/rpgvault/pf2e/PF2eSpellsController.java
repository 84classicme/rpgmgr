package io.festerson.rpgvault.pf2e;

import io.festerson.rpgvault.domain.pf2api.SpellResponse;
import io.festerson.rpgvault.domain.pf2api.SpellResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/pf2e")
public class PF2eSpellsController {

    @Autowired
    PF2eService pf2eService;

//    @GetMapping("/spells/all")
//    public Mono<ResponseEntity<List<SpellResult>>> getSpells() {
//        return pf2eService.getSpells()
//            .map(SpellResponse::getResults)
//            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
//    }

    @GetMapping("/spells")
    public Mono<ResponseEntity<List<SpellResult>>> getSpellByName(@RequestParam(value="name", required=false) String name) {
        if(name == null || name.isBlank()) {
            return pf2eService.getAllSpellsByLevelAndName()
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("RpgMgrMessage", "Server error fetching all spells: " + name)
                    .build());
        }
        return pf2eService.getSpellByName(name)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error searching spells by name: " + name)
                .build());
    }

    @GetMapping("/spells/{id}")
    public Mono<ResponseEntity<SpellResult>> getSpellById(@PathVariable String id) {
        return pf2eService.getSpellById(id)
            .map(spell -> ResponseEntity.ok().body(spell))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching spell id: " + id)
                .build());
    }

    @GetMapping("/spells/tradition/{tradition}")
    public Mono<ResponseEntity<List<SpellResult>>> getSpellByTradition(@PathVariable String tradition) {
        return pf2eService.getSpellByTradition(tradition)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching spell tradition: " + tradition)
                .build());
    }

    @GetMapping("/spells/load")
    public Mono<ResponseEntity<Void>>loadAllSpellsIntoDb(){
        return pf2eService.getSpellsFromExternalSource()
            .map(SpellResponse::getResults)
            .flatMap(spellResults -> pf2eService.saveSpells(spellResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e spells.")
                .build());
    }
}
