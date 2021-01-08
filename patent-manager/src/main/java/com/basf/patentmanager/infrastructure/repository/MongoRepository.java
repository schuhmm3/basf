package com.basf.patentmanager.infrastructure.repository;

import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.repository.PatentRepository;
import com.basf.patentmanager.infrastructure.entity.PatentEntity;
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class MongoRepository implements PatentRepository {

    private final SpringDataMongoRepository springDataMongoRepository;
    private final JMapper<Patent, PatentEntity> patentEntityMapper;
    private final JMapper<PatentEntity, Patent> patentMapper;

    @Autowired
    public MongoRepository(SpringDataMongoRepository springDataMongoRepository) {
        this.springDataMongoRepository = springDataMongoRepository;
        this.patentEntityMapper = new JMapper<>(Patent.class, PatentEntity.class);
        this.patentMapper = new JMapper<>(PatentEntity.class, Patent.class);
    }

    @Override
    public Mono<Patent> findById(UUID uuid) {
        return this.springDataMongoRepository
                .findById(uuid)
                .map(this.patentEntityMapper::getDestination);
    }

    @Override
    public Mono<Patent> save(Patent patent) {
        return this.springDataMongoRepository
                .save(this.patentMapper.getDestination(patent))
                .map(this.patentEntityMapper::getDestination);
    }

}
