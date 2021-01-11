package com.basf.patentmanager.domain.service;

import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.repository.PatentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DomainPatentService implements PatentService {

    private final PatentRepository patentRepository;

    @Autowired
    public DomainPatentService(PatentRepository patentRepository){
        this.patentRepository = patentRepository;
    }

    @Override
    public Mono<Patent> addPatent(Patent patent) {
        return this.patentRepository.save(patent);
    }

    @Override
    public Mono<Patent> findPatent(UUID id) {
        return this.patentRepository.findById(id);
    }
}
