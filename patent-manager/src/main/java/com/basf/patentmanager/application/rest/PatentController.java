package com.basf.patentmanager.application.rest;

import com.basf.patentmanager.application.model.rest.ProcessRequest;
import com.basf.patentmanager.application.service.ApplicationPatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/patent")
public class PatentController {

    private final ApplicationPatentService patentService;

    @Autowired
    public PatentController(ApplicationPatentService patentService) {
        this.patentService = patentService;
    }

    @PostMapping(name = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Boolean> process(@ModelAttribute ProcessRequest processRequest) {
        return this.patentService.upload(processRequest.getFile());
    }

}
