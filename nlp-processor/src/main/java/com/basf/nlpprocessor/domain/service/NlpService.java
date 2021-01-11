package com.basf.nlpprocessor.domain.service;

import com.basf.nlpprocessor.domain.model.Document;
import reactor.core.publisher.Mono;

/**
 * Interfaces that defines the API for the services that runs the NLP process
 *
 * @author robertogomez
 */
public interface NlpService {

    Mono<Document> processNlp(String text);
}
