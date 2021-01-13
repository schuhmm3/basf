package com.basf.patentmanager.infrastructure.entity;

import com.basf.patentmanager.domain.model.Entity;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Dto to build the received message in the asynchronous communication with the Nlp Service
 */
@Data
public class EntityEventOutput {
    private final UUID id;
    private final List<Entity> entities;
}
