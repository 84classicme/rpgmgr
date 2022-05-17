package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.CClass;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DndClassRepository extends ReactiveMongoRepository<CClass, String> {
    public Flux<CClass> findAll();

}
