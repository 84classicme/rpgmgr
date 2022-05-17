package io.festoso.rpgvault.pf2e;

import io.festoso.rpgvault.domain.pf2api.FeatResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PF2eFeatRepository extends ReactiveMongoRepository<FeatResult, String> {

}
