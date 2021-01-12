package com.basf.patentmanager.domain.service;

import com.basf.patentmanager.domain.api.NlpApi;
import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.repository.PatentRepository;
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
    private final NlpApi nlpApi;

    /**
     * Process and stores the patent in the repository
     *
     * @param patent Patent to process and store
     * @return {@link Mono} emitting the saved {@link Patent} or {@link Mono#empty()} if none.
     */
    @Override
    public Mono<Patent> addPatent(Patent patent) {
        patent.setEntities(this.nlpApi.getEntities(patent.getText()));
        log.debug("Entities found: {}", patent.getEntities());
        return this.patentRepository.save(patent);
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
