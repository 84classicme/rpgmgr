package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Skill;
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
public class DndSkillController {

    @Autowired
    DndService dndService;

    @GetMapping("/skills")
    public Mono<ResponseEntity<List<Skill>>> getAllSkills() {
        return dndService.getAllSkills()
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting all dnd skills.")
                .build());
    }

    @GetMapping("/skills/{name}")
    public Mono<ResponseEntity<Skill>> getSkill(@PathVariable String name) {
        return dndService.getSkill(name)
            .map(feature -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(feature))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting skill with name." + name)
                .build());
    }
}
