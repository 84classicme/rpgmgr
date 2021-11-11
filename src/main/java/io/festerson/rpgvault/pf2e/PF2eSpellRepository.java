package io.festerson.rpgvault.pf2e;

import io.festerson.rpgvault.domain.pf2api.SpellResult;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PF2eSpellRepository extends ReactiveMongoRepository<SpellResult, String> {

    @Query("{ 'name': {'$regex': ?0, $options: 'i'} }")    // case insensitive "like" query
    public Flux<SpellResult> getSpellByName(String name);

    @Query("{ '_id': ?0 }")
    public Mono<SpellResult> getSpellById(String id);

    @Query("{ 'data.traditions.value': ?0 }")
    public Flux<SpellResult> getSpellByTradition(String tradition);
}
