package io.festerson.rpgvault.pf2e;

import io.festerson.rpgvault.domain.pf2api.SpellResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PF2eSpellRepository extends ReactiveMongoRepository<SpellResult, String> {

}
