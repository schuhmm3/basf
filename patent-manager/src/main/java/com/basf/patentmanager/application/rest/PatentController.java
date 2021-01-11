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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Rest controller to handle all the patents
 *
 * @author robertogomez
 */
@RestController
@RequestMapping("/api/patent")
public class PatentController {

    private final ApplicationPatentService patentService;

    @Autowired
    public PatentController(ApplicationPatentService patentService) {
        this.patentService = patentService;
    }

    @Operation(summary = "Uploads multiple patents inside a ZIP file")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> process(@ModelAttribute UploadRequest request) {
        PatentFieldsPaths paths = new JMapper<>(PatentFieldsPaths.class, UploadRequest.class).getDestination(request);
        if (paths.pathsAreValid())
            return this.patentService.upload(request.getFile(), paths);
        else
            throw new PatentException(ApplicationError.INVALID_XML_PATHS);
    }

}
