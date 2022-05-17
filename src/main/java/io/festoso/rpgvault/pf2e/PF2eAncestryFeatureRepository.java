package io.festoso.rpgvault.pf2e;

import io.festoso.rpgvault.domain.pf2api.AncestryFeatureResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PF2eAncestryFeatureRepository extends ReactiveMongoRepository<AncestryFeatureResult, String> {

}
