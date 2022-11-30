package io.festoso.rpgvault.campaigns;

import io.festoso.rpgvault.domain.Campaign;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CampaignRepository extends ReactiveMongoRepository<Campaign, String> {

    Flux<String> findPlayerIdsById(String id);

    @Query("{ 'playerIds': ?0 }")
    Flux<Campaign> getCampaignsByPlayerId(String campaignId);

    Mono<Campaign> findByName(String name);
}
