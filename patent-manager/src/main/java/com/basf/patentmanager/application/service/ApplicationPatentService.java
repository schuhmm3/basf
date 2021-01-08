package com.basf.patentmanager.application.service;

import com.basf.patentmanager.domain.service.PatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ApplicationPatentService {

    private final PatentService patentService;

    @Autowired
    public ApplicationPatentService(PatentService patentService){
        this.patentService = patentService;
    }

    public Mono<Boolean> upload(FilePart filePart){
        return Mono.just(true);
    }
}
