package com.basf.patentmanager.domain.service;

import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.repository.PatentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Domain Service that implements {@link PatentService} to store the patents
 *
 * @author robertogomez
 */
@Service
public class DomainPatentService implements PatentService {

    private final PatentRepository patentRepository;

    @Autowired
    public DomainPatentService(PatentRepository patentRepository){
        this.patentRepository = patentRepository;
    }

    /**
     * Process and stores the patent in the repository
     *
     * @param patent Patent to process and store
     *
     * @return {@link Mono} emitting the saved {@link Patent} or {@link Mono#empty()} if none.
     */
    @Override
    public Mono<Patent> addPatent(Patent patent) {
        return this.patentRepository.save(patent);
    }

    /**
     * Finds a patent by its UUID
     *
     * @param id Id to find the patent in the repository
     *
     * @return {@link Mono} emitting the found {@link Patent}.
     */
    @Override
    public Mono<Patent> findPatent(UUID id) {
        return this.patentRepository.findById(id);
    }
}
