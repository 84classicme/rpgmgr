package io.festoso.rpgvault.dnd;

import io.festoso.rpgvault.domain.dnd.EquipmentDetail;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DndEquipmentRepository extends ReactiveMongoRepository<EquipmentDetail, String> {

    @Query(value ="{ 'name': {'$regex': ?0, $options: 'i'} }")
    public Mono<EquipmentDetail> findByName(String name);
}
