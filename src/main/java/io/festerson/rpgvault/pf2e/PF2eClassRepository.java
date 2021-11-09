package io.festerson.rpgvault.pf2e;

import io.festerson.rpgvault.domain.pf2api.ClassResult;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PF2eClassRepository extends ReactiveMongoRepository<ClassResult, String> {

}
