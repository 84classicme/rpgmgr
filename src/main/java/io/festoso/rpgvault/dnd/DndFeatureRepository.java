package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Feature;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DndFeatureRepository extends ReactiveMongoRepository<Feature, String> {

    public Mono<Feature> findByIndex(String index);
}
