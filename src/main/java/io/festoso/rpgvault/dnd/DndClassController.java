package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.CClass;
import io.festoso.rpgvault.domain.dnd.Level;
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
public class DndClassController {

    @Autowired
    DndService dndService;

    @GetMapping("/classes")
    public Mono<ResponseEntity<List<CClass>>> getClasses() {
        return dndService.getAllClasses()
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd classes.")
                .build());
    }

    @GetMapping("/classes/{name}/levels")
    public Mono<ResponseEntity<List<Level>>> getLevelsForClass(@PathVariable String name) {
        return dndService.getLevelsForClass(name)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd classes.")
                .build());
    }

}
