package io.festerson.rpgvault.campaigns;

import io.festerson.rpgvault.domain.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    public Flux<Campaign> getCampaigns(){
        return campaignRepository.findAll();
    }

    public Flux<Campaign> getCampaignsByPlayerId(String id){
        return campaignRepository.getCampaignsByPlayerId(id);
    }

    public Mono<Campaign> getCampaignById(String id){
        return campaignRepository.findById(id);
    }

    public Mono<Campaign> createCampaign(Campaign campaign){
        return campaignRepository.save(campaign).map(campaign1 -> campaign1);
    }

    public Mono<Campaign> updateCampaign(String id, Campaign campaign){
        return campaignRepository.findById(id)
            .flatMap(found -> {
                if(campaign.getName() != null && !campaign.getName().isEmpty())
                    found.setName(campaign.getName());
                if(campaign.getStartDate() != null)
                    found.setStartDate(campaign.getStartDate());
                if(campaign.getEndDate() != null)
                    found.setEndDate(campaign.getEndDate());
                if(campaign.getPlayerIds() != null)
                    found.setPlayerIds(campaign.getPlayerIds());
                if(campaign.getCharacterIds() != null)
                    found.setCharacterIds(campaign.getCharacterIds());
                if(campaign.getNpcIds() != null)
                    found.setNpcIds(campaign.getNpcIds());
                if(campaign.getMonsterIds() != null)
                    found.setMonsterIds(campaign.getMonsterIds());
                if(campaign.getDmId() != null && !campaign.getDmId().isEmpty())
                    found.setDmId(campaign.getDmId());
                if(campaign.getDescription() != null && !campaign.getDescription().isEmpty())
                    found.setDescription(campaign.getDescription());
                if(campaign.getImageUrl() != null && !campaign.getImageUrl().isEmpty())
                    found.setImageUrl(campaign.getImageUrl());
                return Mono.just(found);
            })
            .flatMap(updatedCampaign -> campaignRepository.save(updatedCampaign));
    }

    public Mono<Void> deleteCampaign(String id){
        return campaignRepository.findById(id)
            .flatMap(toDelete ->
                campaignRepository.delete(toDelete));
    }

}