package com.basf.patentmanager.infrastructure.repository;

import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.repository.PatentRepository;
import com.basf.patentmanager.infrastructure.entity.MongoPatentEntity;
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Interfaces that implements {@link PatentRepository} to store the patents in MongoDB
 *
 * @author robertogomez
 */
@Repository
public class MongoRepository implements PatentRepository {

    private final SpringDataMongoRepository springDataMongoRepository;
    private final JMapper<Patent, MongoPatentEntity> patentEntityMapper;
    private final JMapper<MongoPatentEntity, Patent> patentMapper;

    @Autowired
    public MongoRepository(SpringDataMongoRepository springDataMongoRepository) {
        this.springDataMongoRepository = springDataMongoRepository;
        this.patentEntityMapper = new JMapper<>(Patent.class, MongoPatentEntity.class);
        this.patentMapper = new JMapper<>(MongoPatentEntity.class, Patent.class);
    }

    /**
     * Saves a {@link Patent} into MongoDB
     *
     * @param patent patent to store
     * @return {@link Mono} emitting the saved {@link Patent}.
     */
    public Mono<Patent> save(Patent patent) {
        return this.springDataMongoRepository
                .save(this.patentMapper.getDestination(patent))
                .map(this.patentEntityMapper::getDestination);
    }

    /**
     * Finds a {@link Patent} by its UUID
     *
     * @param uuid id to find the patent
     * @return {@link Mono} emitting the found {@link Patent} or {@link Mono#empty()} if none.
     */
    public Mono<Patent> findById(UUID uuid) {
        return this.springDataMongoRepository
                .findById(uuid)
                .map(this.patentEntityMapper::getDestination);
    }

    /**
     * Finds the {@link Patent} by its application reference
     *
     * @param application application number to find the patent
     * @return {@link Flux} emitting the {@link Patent} found or {@link Mono#empty()} if none.
     */
    public Flux<Patent> findByApplication(String application) {
        return this.springDataMongoRepository
                .findByApplication(application)
                .map(this.patentEntityMapper::getDestination);
    }

    /**
     * Deletes the {@link Patent} by its application reference
     *
     * @param uuid id to delete the patent
     * @return {@link Mono} when the operation is completed.
     */
    public Mono<Void> deleteById(UUID uuid) {
        return this.springDataMongoRepository
                .deleteById(uuid);
    }

    /**
     * Deletes all the {@link Patent}
     *
     * @return {@link Mono} when the operation is completed.
     */
    public Mono<Void> deleteAll() {
        return this.springDataMongoRepository.deleteAll();
    }

}
