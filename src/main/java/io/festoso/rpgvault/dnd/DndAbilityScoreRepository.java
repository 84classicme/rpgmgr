package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.AbilityScore;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DndAbilityScoreRepository extends ReactiveMongoRepository<AbilityScore, String> {

    public Mono<AbilityScore> findByIndex(String index);
}
