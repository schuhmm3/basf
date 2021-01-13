package com.basf.nlpprocessor.application.service;

import com.basf.nlpprocessor.domain.model.Document;
import com.basf.nlpprocessor.domain.model.Entity;
import com.basf.nlpprocessor.domain.service.NlpService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service to run the NLP process on a document
 *
 * @author robertogomez
 */
@Service
public class ApplicationNlpService {

    private final NlpService nlpService;

    public ApplicationNlpService(NlpService nlpService) {
        this.nlpService = nlpService;
    }

    /**
     * Process the text with an NLP Pipeline
     *
     * @param text Text to process
     * @return {@link Mono} emitting the {@link Document} with the result of the NLP process or {@link Mono#empty()} if none.
     */
    public Mono<Document> processDocument(String text) {
        return this.nlpService.processNlp(text);
    }

    /**
     * Process the text with an NLP Pipeline and retrieves the entities
     *
     * @param text Text to process
     * @return {@link Flux} emitting the multiple {@link Entity} retrieved.
     */
    public Flux<Entity> getEntities(String text) {
        return this.nlpService.processNlp(text).flatMapIterable(Document::getEntities);
    }
}
