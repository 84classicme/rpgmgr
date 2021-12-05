package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.Trait;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DndTraitRepository extends ReactiveMongoRepository<Trait, String> {

    public Mono<Trait> findByIndex(String index);
}
