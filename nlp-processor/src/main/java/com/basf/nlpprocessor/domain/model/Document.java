package com.basf.nlpprocessor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Document entity that stores the result of the NLP process
 *
 * @author robertogomez
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Document {

    @NonNull
    List<Token> tokens;

    @NonNull
    String text;

    List<Entity> entities;

}
