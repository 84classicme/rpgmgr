package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.EquipmentDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/dnd")
public class DndEquipmentController {

    @Autowired
    DndService dndService;

    @GetMapping("/equipment/{name}")
    public Mono<ResponseEntity<EquipmentDetail>> getAbility(@PathVariable String name) {
        return dndService.getEquipment(name)
            .map(feature -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(feature))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error getting equipment with name." + name)
                .build());
    }

}
