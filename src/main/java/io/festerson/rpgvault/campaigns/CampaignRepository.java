package io.festerson.rpgvault.campaigns;

import io.festerson.rpgvault.domain.Campaign;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CampaignRepository extends ReactiveMongoRepository<Campaign, String> {

    @Query("{ 'playerIds': ?0 }")
    Flux<Campaign> getCampaignsByPlayerId(String campaignId);
}
