package io.festerson.rpgvault.characters;

import io.festerson.rpgvault.domain.PlayerCharacter;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CharacterRepository extends ReactiveMongoRepository<PlayerCharacter, String> {

        @Query("{ 'name': ?0 }")
        Mono<PlayerCharacter> findByName(String name);

        @Query("{ 'playerId': ?0 }")
        Flux<PlayerCharacter> getCharactersByPlayerId(String playerId);
}
