package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.Skill;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DndSkillRepository extends ReactiveMongoRepository<Skill, String> {

    public Flux<Skill> findAll();

    public Mono<Skill> findByIndex(String index);
}
