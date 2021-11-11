package io.festerson.rpgvault.pf2e;

import io.festerson.rpgvault.domain.pf2api.*;
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
public class PF2eController {

    @Autowired
    PF2eService pf2eService;

    @Autowired
    PF2eSpellRepository pf2eSpellRepository;

    @GetMapping("/actions/load")
    public Mono<ResponseEntity<Void>>loadAllActionsIntoDb(){
        return pf2eService.getActions()
            .map(ActionResponse::getResults)
            .flatMap(actionResults -> pf2eService.saveActions(actionResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e actions.")
                .build());
    }

    @GetMapping("/ancestries/load")
    public Mono<ResponseEntity<Void>>loadAllAncestriesIntoDb(){
        return pf2eService.getAncestries()
            .map(AncestryResponse::getResults)
            .flatMap(ancestryResults -> pf2eService.saveAncestries(ancestryResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e ancestries.")
                .build());
    }

    @GetMapping("/ancestryFeatures/load")
    public Mono<ResponseEntity<Void>>loadAllAncestryFeaturesIntoDb(){
        return pf2eService.getAncestryFeatures()
            .map(AncestryFeatureResponse::getResults)
            .flatMap(ancestryFeatureResults -> pf2eService.saveAncestryFeatures(ancestryFeatureResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e ancestry features.")
                .build());
    }

    @GetMapping("/archetypes/load")
    public Mono<ResponseEntity<Void>>loadAllArchetypesIntoDb(){
        return pf2eService.getArchetypes()
            .map(ArchetypeResponse::getResults)
            .flatMap(archetypeResults -> pf2eService.saveArchetypes(archetypeResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e archetypes.")
                .build());
    }

    @GetMapping("/backgrounds/load")
    public Mono<ResponseEntity<Void>>loadAllBackgroundsIntoDb(){
        return pf2eService.getBackgrounds()
            .map(BackgroundResponse::getResults)
            .flatMap(backgroundResults -> pf2eService.saveBackgrounds(backgroundResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e backgrounds.")
                .build());
    }

    @GetMapping("/classes/load")
    public Mono<ResponseEntity<Void>>loadAllClassesIntoDb(){
        return pf2eService.getClasses()
            .map(ClassResponse::getResults)
            .flatMap(classResults -> pf2eService.saveClasses(classResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e classes.")
                .build());
    }

    @GetMapping("/classFeatures/load")
    public Mono<ResponseEntity<Void>>loadAllClassFeaturesIntoDb(){
        return pf2eService.getClassFeatures()
            .map(ClassFeatureResponse::getResults)
            .flatMap(classFeatureResults -> pf2eService.saveClassFeatures(classFeatureResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e classes.")
                .build());
    }

    @GetMapping("/deities/load")
    public Mono<ResponseEntity<Void>>loadAllDeitiesIntoDb(){
        return pf2eService.getDeities()
            .map(DeityResponse::getResults)
            .flatMap(deityResults -> pf2eService.saveDeities(deityResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e classes.")
                .build());
    }

    @GetMapping("/equipment/load")
    public Mono<ResponseEntity<Void>>loadAllEquipmentIntoDb(){
        return pf2eService.getEquipment()
            .map(EquipmentResponse::getResults)
            .flatMap(equipmentResults -> pf2eService.saveEquipment(equipmentResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e equipment.")
                .build());
    }

    @GetMapping("/feats/load")
    public Mono<ResponseEntity<Void>>loadAllFeatsIntoDb(){
        return pf2eService.getFeats()
            .map(FeatResponse::getResults)
            .flatMap(featResults -> pf2eService.saveFeats(featResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e equipment.")
                .build());
    }

    @GetMapping("/spells/all")
    public Mono<ResponseEntity<List<SpellResult>>> getSpells() {
        return pf2eService.getSpells()
            .map(SpellResponse::getResults)
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
    }

    @GetMapping("/spells")
    public Mono<ResponseEntity<List<SpellResult>>> getSpellByName(@RequestParam(value="name") String name) {
        return pf2eService.getSpellByName(name)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching spell name: " + name)
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
        return pf2eService.getSpells()
            .map(SpellResponse::getResults)
            .flatMap(spellResults -> pf2eService.saveSpells(spellResults))
            .thenReturn(ResponseEntity.ok().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error loading PF2e spells.")
                .build());
    }
}
