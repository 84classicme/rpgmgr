package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Background;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DndBackgroundRepository extends ReactiveMongoRepository<Background, String> {
    public Flux<Background> findAll();
}
