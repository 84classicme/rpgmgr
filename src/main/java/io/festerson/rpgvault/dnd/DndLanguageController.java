package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.Language;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/dnd")
public class DndLanguageController {

    @Autowired
    DndService dndService;

    @GetMapping("/languages")
    public Mono<ResponseEntity<List<Language>>> getAllLanguages() {
        return dndService.getAllLanguages()
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd languages.")
                .build());
    }

    @GetMapping("/languages/{index}")
    public Mono<ResponseEntity<Language>> getLanguageByIndex(@PathVariable String index) {
        return dndService.getLanguageByIndex(index)
            .map(feature -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(feature))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting language by index." + index)
                .build());
    }

}
