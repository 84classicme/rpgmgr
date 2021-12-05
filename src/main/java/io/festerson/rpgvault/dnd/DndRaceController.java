package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.Race;
import io.festerson.rpgvault.domain.dnd.Subrace;
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
@RequestMapping("/dnd")
public class DndRaceController {

    @Autowired
    DndService dndService;

    @GetMapping("/races")
    public Mono<ResponseEntity<List<Race>>> getRaces() {
        return dndService.getAllRaces()
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd races.")
                .build());
    }

    @GetMapping("/races/{race}/subraces")
    public Mono<ResponseEntity<List<Subrace>>> getSubrace(@PathVariable String race) {
        return dndService.getAllSubraces(race)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd subraces for race " + race)
                .build());
    }

}
