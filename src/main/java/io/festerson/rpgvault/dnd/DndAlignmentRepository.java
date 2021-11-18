package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.Alignment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DndAlignmentRepository extends ReactiveMongoRepository<Alignment, String> {
    public Flux<Alignment> findAll();
}
