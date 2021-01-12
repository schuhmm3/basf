package com.basf.patentmanager.infrastructure.api;

import com.basf.patentmanager.application.exception.ApplicationError;
import com.basf.patentmanager.application.exception.PatentException;
import com.basf.patentmanager.domain.api.NlpApi;
import com.basf.patentmanager.domain.model.Entity;
import com.basf.patentmanager.infrastructure.config.NlpProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Interfaces that implements {@link NlpApi} to call our NLP microservice
 *
 * @author robertogomez
 */
@Component
@Slf4j
public class BasfNlpApi implements NlpApi {

    private final String nerUri;
    private final WebClient webClient;

    public BasfNlpApi(NlpProperties nlpProperties) {
        this.nerUri = nlpProperties.getNerEndpoint();
        this.webClient = WebClient.builder().baseUrl(this.buildBaseURI(nlpProperties)).build();
    }

    /**
     * Builds base uri for the external service
     *
     * @param nlpProperties properties with the parameters information
     * @return The base URI for the external service as String
     */
    private String buildBaseURI(NlpProperties nlpProperties) {
        return String.format("%s://%s:%s",
                nlpProperties.getProtocol(),
                nlpProperties.getHostname(),
                nlpProperties.getPort()
        );
    }

    /**
     * Call the external service to extract the entities from the text
     *
     * @param text Text to process with the external service
     * @return The {@link List} with the extracted entities
     */
    public List<Entity> getEntities(String text) {
        if (text != null && !text.isEmpty()) {
            log.info("Calling nlp service");
            return this.webClient
                    .post()
                    .uri(this.nerUri)
                    .body(BodyInserters.fromValue(new NerRequest(new String(text.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8))))
                    .retrieve()
                    .onStatus(s -> s.is5xxServerError() || s.is4xxClientError(), e -> Mono.error(new PatentException(ApplicationError.EXTERNAL_NLP_SERVICE_ERROR)))
                    .bodyToFlux(Entity.class)
                    .collectList()
                    .block();
        }else {
            log.warn("Cannot get entities when text is empty or nullable");
            return new ArrayList<>();
        }
    }

    /**
     * Dto to build the request to get the entities
     */
    @Data
    @AllArgsConstructor
    private static class NerRequest {
        private String text;
    }
}
