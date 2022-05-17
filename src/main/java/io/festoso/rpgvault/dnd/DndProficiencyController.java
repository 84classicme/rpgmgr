package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Proficiency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/dnd")
public class DndProficiencyController {

    @Autowired
    DndService dndService;

    @GetMapping("/proficiencies")
    public Mono<ResponseEntity<List<Proficiency>>> getProficiencies(@RequestParam(value="type", required=false) String type) {
        if(type == null || type.isBlank()) {
            return dndService.getAllProficiencies()
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("RpgMgrMessage", "Server error getting all dnd proficiencies.")
                    .build());
        }
        return dndService.getProficienciesByType(type)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd proficiency with type " + type)
                .build());
    }

    @GetMapping("/proficiencies/othertools")
    public Mono<ResponseEntity<List<Proficiency>>> getOtherTools() {
        return dndService.getOtherToolProficiencies()
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd othertool proficiencies.")
                .build());
    }

}
