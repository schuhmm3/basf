package com.basf.patentmanager.application.rest;

import com.basf.patentmanager.application.exception.ApplicationError;
import com.basf.patentmanager.application.exception.PatentException;
import com.basf.patentmanager.application.model.dto.PatentFieldsPaths;
import com.basf.patentmanager.application.model.rest.UploadRequest;
import com.basf.patentmanager.application.service.ApplicationPatentService;
import com.googlecode.jmapper.JMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/api/patent")
public class PatentController {

    private final ApplicationPatentService patentService;

    @Autowired
    public PatentController(ApplicationPatentService patentService) {
        this.patentService = patentService;
    }

    @Operation(summary = "Uploads a patent as XML or multiple patents inside a ZIP file")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> process(@ModelAttribute UploadRequest request) {
        PatentFieldsPaths paths = new JMapper<>(PatentFieldsPaths.class, UploadRequest.class).getDestination(request);
        if (paths.pathsAreValid())
            return this.patentService.upload(request.getFile(), paths);
        else
            throw new PatentException(ApplicationError.INVALID_XML_PATHS);
    }

}
