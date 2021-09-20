package io.festerson.rpgvault.players;

import io.festerson.rpgvault.domain.Player;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PlayerRepository extends ReactiveMongoRepository<Player, String> {

}
