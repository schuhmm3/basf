package com.basf.patentmanager.infrastructure.repository;

import com.basf.patentmanager.infrastructure.entity.PatentEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataMongoRepository extends ReactiveMongoRepository<PatentEntity, UUID> {
}
