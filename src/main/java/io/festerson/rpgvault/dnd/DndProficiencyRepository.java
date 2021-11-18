package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.Proficiency;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DndProficiencyRepository extends ReactiveMongoRepository<Proficiency, String> {

    public Flux<Proficiency> findAll();

    @Query(value ="{ 'type': ?0 }", sort = "{'name': 1}")
    public Flux<Proficiency> findAllByType(String type);
}
