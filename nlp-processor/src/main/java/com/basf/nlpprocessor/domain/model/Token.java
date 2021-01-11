package com.basf.nlpprocessor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Entity that defines all the properties for each token in the document
 *
 * @author robertogomez
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Token {

    @NonNull
    private String text;

    private String tag;

    private String lemma;

    private String entity;
}
