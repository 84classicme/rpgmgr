package io.festoso.rpgvault.pf2e;

import io.festoso.rpgvault.domain.pf2api.ActionResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PF2eActionRepository extends ReactiveMongoRepository<ActionResult, String> {

}
