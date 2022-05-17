package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Level;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DndLevelRepository  extends ReactiveMongoRepository<Level, String> {

    @Query(value ="{ 'class.name': {'$regex': ?0, $options: 'i'} , 'subclass': null}", sort = "{'level': 1}")
    public Flux<Level> getLevelsForClass(String name);
}
