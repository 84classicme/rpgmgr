package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Subrace;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DndSubraceRepository extends ReactiveMongoRepository<Subrace, String> {

    @Query("{'race.index': ?0 }")
    public Flux<Subrace> findAllByRaceIndex(String index);
}
