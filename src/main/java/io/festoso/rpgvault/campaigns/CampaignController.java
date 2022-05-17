package io.festoso.rpgvault.campaigns;

import io.festoso.rpgvault.domain.Campaign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
public class CampaignController {

    private CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/campaigns")
    public Mono<ResponseEntity<List<Campaign>>>  getCampaigns(@RequestParam(value="player", required=false) String playerId) {
        if (playerId == null || playerId.isEmpty()) {
            return campaignService.getCampaigns()
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
                .onErrorReturn(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("RpgMgrMessage", "Server error fetching all campaigns")
                    .build());
        }
        return campaignService.getCampaignsByPlayerId(playerId)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list))
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching campaigns for player id: " + playerId)
                .build());
    }

    @GetMapping("/campaigns/{campaignId}")
    public Mono<ResponseEntity<Campaign>> getCampaign(@PathVariable String campaignId) {
        return campaignService.getCampaignById(campaignId)
            .map(character -> ResponseEntity.ok().body(character))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error fetching campaign id: " + campaignId)
                .build());
    }

    @PostMapping(value="/campaigns", consumes = "application/json")
    public Mono<ResponseEntity<Campaign>> saveCampaign(@Valid @RequestBody Campaign campaign) {
        return campaignService.createCampaign(campaign)
            .map(saved -> ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(saved))
            .defaultIfEmpty(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error creating campaign: " + campaign)
                .build());
    }

    @PutMapping("/campaigns/{campaignId}")
    public Mono<ResponseEntity<Campaign>> updateCampaign(@Valid @RequestBody Campaign campaign, @PathVariable String campaignId){
        return campaignService.updateCampaign(campaignId, campaign)
            .map(updatedCharacter -> ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedCharacter))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error updating campaign: " + campaign)
                .build());
    }

    @DeleteMapping("/campaigns/{campaignId}")
    public  Mono<ResponseEntity<Void>> deleteCampaign(@PathVariable String campaignId){
        return campaignService.deleteCampaign(campaignId)
            .thenReturn(ResponseEntity.noContent().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().<Void>build())
            .onErrorReturn(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("RpgMgrMessage", "Server error deleting campaign id: " + campaignId)
                .build());
    }


}
