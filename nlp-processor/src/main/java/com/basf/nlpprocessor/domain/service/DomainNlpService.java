package com.basf.nlpprocessor.domain.service;

import com.basf.nlpprocessor.domain.api.NlpApi;
import com.basf.nlpprocessor.domain.model.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Domain Service that implements {@link NlpService} to run the the NLP process
 *
 * @author robertogomez
 */
@Service
public class DomainNlpService implements NlpService {

    private final NlpApi nlpApi;

    public DomainNlpService(NlpApi nlpApi) {
        this.nlpApi = nlpApi;
    }

    /**
     * Process the text with an NLP Pipeline
     *
     * @param text Text to process
     * @return {@link Mono} emitting the {@link Document} with the result of the NLP process or {@link Mono#empty()} if none.
     */
    public Mono<Document> processNlp(String text) {
        return this.nlpApi.processNlp(text);
    }

}
