package com.basf.nlpprocessor.domain.api;

import com.basf.nlpprocessor.domain.model.Document;
import reactor.core.publisher.Mono;

/**
 * Interface that defines the API to run the NLP frameworks
 *
 * @author robertogomez
 */
public interface NlpApi {

    Mono<Document> processNlp(String text);

}
