package com.basf.patentmanager.domain.service;

import com.basf.patentmanager.domain.model.Patent;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PatentService {

    Mono<Patent> addPatent(Patent patent);

    Mono<Patent> findPatent(UUID id);
}
