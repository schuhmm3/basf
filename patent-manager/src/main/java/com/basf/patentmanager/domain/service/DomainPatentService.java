package com.basf.patentmanager.domain.service;

import com.basf.patentmanager.application.exception.ApplicationError;
import com.basf.patentmanager.application.exception.PatentException;
import com.basf.patentmanager.domain.api.NlpApi;
import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.repository.PatentRepository;
import com.basf.patentmanager.infrastructure.entity.EntityEventInput;
import com.basf.patentmanager.infrastructure.repository.AsynchronousRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Domain Service that implements {@link PatentService} to store the patents
 *
 * @author robertogomez
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class DomainPatentService implements PatentService {

    private final PatentRepository patentRepository;
    private final AsynchronousRepository asynchronousRepository;
    private final NlpApi nlpApi;

    /**
     * Process and stores the patent in the repository
     *
     * @param patent Patent to process and store
     * @param async Flag to process patent asynchronous
     * @return {@link Mono} emitting the saved {@link Patent} or {@link Mono#empty()} if none.
     */
    @Override
    public Mono<Patent> addPatent(Patent patent, boolean async) {
        if (async) {
            log.info("Processing patent async");
            return addPatentAsync(patent);
        }else{
            log.info("Processing patent synchronously");
            return this.addPatentSync(patent);
        }
    }

    /**
     * Processes the patent in a synchronous way by calling through the REST API the Nlp Service and stores it in the repository
     *
     * @param patent Patent to process and store
     * @return {@link Mono} emitting the saved {@link Patent} or {@link Mono#empty()} if none.
     */
    private Mono<Patent> addPatentSync(Patent patent) {
        patent.setEntities(this.nlpApi.getEntities(patent.getText()));
        log.debug("Entities found: {}", patent.getEntities());
        return this.patentRepository.save(patent);
    }

    /**
     * Processes the patent in an asynchronous way by sending a message to the Nlp Service and stores it in the repository
     *
     * @param patent Patent to process and store
     * @return {@link Mono} emitting the saved {@link Patent} or {@link Mono#empty()} if none.
     */
    private Mono<Patent> addPatentAsync(Patent patent) {
        return this.patentRepository.save(patent).doOnSuccess(p -> {
            try {
                asynchronousRepository.getEntityEventInputs().put(new EntityEventInput(p.getUuid(),p.getText()));
            } catch (InterruptedException e) {
                log.warn("Interrupted exception when sending event");
                Thread.currentThread().interrupt();
                throw new PatentException(ApplicationError.INTERNAL_ERROR, e);
            }
        });
    }

    /**
     * Finds a patent by its UUID
     *
     * @param id Id to find the patent in the repository
     * @return {@link Mono} emitting the found {@link Patent}.
     */
    @Override
    public Mono<Patent> findPatent(UUID id) {
        return this.patentRepository.findById(id);
    }

    /**
     * Finds the {@link Patent} by its application reference
     *
     * @param application application number to find the patent
     * @return {@link Flux} emitting the {@link Patent} found or {@link Mono#empty()} if none.
     */
    @Override
    public Flux<Patent> findPatent(String application) {
        return this.patentRepository.findByApplication(application);
    }

    /**
     * Deletes the {@link Patent} by its application reference
     *
     * @param id id to delete the patent
     * @return {@link Mono} when the operation is completed.
     */
    @Override
    public Mono<Void> deletePatent(UUID id) {
        return this.patentRepository.deleteById(id);
    }

    /**
     * Deletes all the {@link Patent}
     *
     * @return {@link Mono} when the operation is completed.
     */
    @Override
    public Mono<Void> deleteAll() {
        return this.patentRepository.deleteAll();
    }
}
