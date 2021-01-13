package com.basf.nlpprocessor.application.rest;

import com.basf.nlpprocessor.application.model.rest.ProcessRequest;
import com.basf.nlpprocessor.application.service.ApplicationNlpService;
import com.basf.nlpprocessor.domain.model.Document;
import com.basf.nlpprocessor.domain.model.Entity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Rest controller to run an NLP Pipeline on text
 *
 * @author robertogomez
 */
@RestController
@RequestMapping("/api/document")
public class NlpController {

    private final ApplicationNlpService nlpService;

    @Autowired
    public NlpController(ApplicationNlpService nlpService) {
        this.nlpService = nlpService;
    }

    @Operation(summary = "Process text with the default nlp pipeline")
    @PostMapping(value="/process", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Document> process(@RequestBody ProcessRequest request) {
        return this.nlpService.processDocument(request.getText());
    }

    @Operation(summary = "Gets the recognized entities on the text")
    @PostMapping(value = "/entities", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Entity> getEntities(@RequestBody ProcessRequest request) {
        return this.nlpService.getEntities(request.getText());
    }

}
