package io.festerson.rpgvault.pf2e;

import io.festerson.rpgvault.domain.pf2api.FeatResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PF2eFeatRepository extends ReactiveMongoRepository<FeatResult, String> {

}
