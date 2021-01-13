package com.basf.patentmanager.infrastructure.entity;

import lombok.Data;

import java.util.UUID;

/**
 * Dto to build the message sent to the Nlp Service
 */
@Data
public class EntityEventInput {
    private final UUID id;
    private final String text;
}

