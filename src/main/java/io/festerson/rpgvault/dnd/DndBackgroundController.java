package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.Background;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/dnd")
public class DndBackgroundController {

    @Autowired
    DndService dndService;

    @GetMapping("/backgrounds")
    public Mono<ResponseEntity<List<Background>>> getAllBackgrounds() {
        return dndService.getAllBackgrounds()
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd backgrounds.")
                .build());
    }

}
