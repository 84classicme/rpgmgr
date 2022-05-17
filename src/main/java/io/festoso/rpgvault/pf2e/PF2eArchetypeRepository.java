package io.festoso.rpgvault.pf2e;

import io.festoso.rpgvault.domain.pf2api.ArchetypeResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PF2eArchetypeRepository extends ReactiveMongoRepository<ArchetypeResult, String> {

}
