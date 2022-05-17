package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.Language;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DndLanguageRepository extends ReactiveMongoRepository<Language, String> {
    public Flux<Language> findAll();

    Mono<Language> findByIndex(String index);
}
