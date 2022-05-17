package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Race;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DndRaceRepository extends ReactiveMongoRepository<Race, String> {
    public Flux<Race> findAll();
}
