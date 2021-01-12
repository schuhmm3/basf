package com.basf.patentmanager.infrastructure.repository;

import com.basf.patentmanager.infrastructure.entity.MongoPatentEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Spring data repository to access MongoDB
 *
 * @author robertogomez
 */
@Repository
public interface SpringDataMongoRepository extends ReactiveMongoRepository<MongoPatentEntity, UUID> {

    Flux<MongoPatentEntity> findByApplication(String application);



}
