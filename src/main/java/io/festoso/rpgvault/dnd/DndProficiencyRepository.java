package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Proficiency;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DndProficiencyRepository extends ReactiveMongoRepository<Proficiency, String> {

    public Flux<Proficiency> findAll();

    @Query(value ="{ 'type': ?0 }", sort = "{'name': 1}")
    public Flux<Proficiency> findAllByType(String type);

    @Query(value ="{'type': {$in:['Kits', 'Vehicles', 'Other Tools', 'Gaming Sets']}}", sort = "{'name': 1}")
    Flux<Proficiency> getOtherToolProficiencies();
}
