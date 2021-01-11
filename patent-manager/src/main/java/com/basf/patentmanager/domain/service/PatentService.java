package com.basf.patentmanager.domain.service;

import com.basf.patentmanager.domain.model.Patent;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Interfaces that defines the API for the services that handle the patents
 *
 * @author robertogomez
 */
public interface PatentService {

    Mono<Patent> addPatent(Patent patent);

    Mono<Patent> findPatent(UUID id);
}
