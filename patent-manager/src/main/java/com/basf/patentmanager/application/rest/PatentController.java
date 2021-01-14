package com.basf.patentmanager.application.rest;

import com.basf.patentmanager.application.exception.ApplicationError;
import com.basf.patentmanager.application.exception.PatentException;
import com.basf.patentmanager.application.model.dto.PatentFieldsPaths;
import com.basf.patentmanager.application.model.rest.UploadRequest;
import com.basf.patentmanager.application.service.ApplicationPatentService;
import com.basf.patentmanager.domain.model.Patent;
import com.googlecode.jmapper.JMapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Rest controller to handle all the patents
 *
 * @author robertogomez
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/patent")
public class PatentController {

    private final ApplicationPatentService patentService;

    @Operation(summary = "Uploads multiple patents inside a ZIP file or a single patent in a XML")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> process(@ModelAttribute UploadRequest request) {
        PatentFieldsPaths paths = new JMapper<>(PatentFieldsPaths.class, UploadRequest.class).getDestination(request);
        if (paths.pathsAreValid())
            return this.patentService.uploadPatent(request.getFile(), paths, request.getAsync());
        else
            throw new PatentException(ApplicationError.INVALID_XML_PATHS);
    }

    @Hidden
    @GetMapping
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Flux<String> noContent() {
        return Flux.empty();
    }

    @Operation(summary = "Finds patents by its UUID")
    @GetMapping(value = "/{id}")
    public Flux<Patent> findPatent(@Parameter(description = "UUID or application to find the patent") @PathVariable final String id) {
        try {
            return this.patentService.findPatent(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return this.patentService.findPatent(id);
        }
    }

    @Operation(summary = "Deletes a patent by its UUID")
    @DeleteMapping(value = "/{id}")
    public Mono<Void> deletePatent(@Parameter(description = "UUID to delete the patent") @PathVariable final UUID id) {
        return this.patentService.deletePatent(id);
    }

    @Operation(summary = "Deletes all")
    @DeleteMapping
    public Mono<Void> deleteAll() {
        return this.patentService.deleteAll();
    }

}
