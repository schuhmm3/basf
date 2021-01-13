package com.basf.patentmanager.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Entity that defines the entities extracted by the NER
 *
 * @author robertogomez
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Entity {

    @NonNull
    private String label;

    @NonNull
    private String text;
}
