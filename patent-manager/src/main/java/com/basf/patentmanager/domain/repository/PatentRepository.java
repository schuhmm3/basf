package com.basf.patentmanager.domain.repository;

import com.basf.patentmanager.domain.model.Patent;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PatentRepository {

    Mono<Patent> findById(UUID uuid);
    Mono<Patent> save(Patent patent);
}
