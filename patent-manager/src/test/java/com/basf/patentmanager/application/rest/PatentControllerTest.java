package com.basf.patentmanager.application.rest;

import com.basf.patentmanager.application.exception.ApplicationError;
import com.basf.patentmanager.application.model.dto.PatentFieldsPaths;
import com.basf.patentmanager.application.model.rest.ErrorResponse;
import com.basf.patentmanager.application.service.ApplicationPatentService;
import com.basf.patentmanager.domain.model.Patent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient(timeout = "36000")
@WebFluxTest(PatentController.class)
class PatentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ApplicationPatentService service;

    @Test
     void uploadReturnsOkIfValidRequest() {
        when(service.uploadPatent(any(FilePart.class), any(PatentFieldsPaths.class), any(Boolean.class))).thenReturn(Mono.just(true).then());
        var bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", new ClassPathResource("US06193294B1.xml"));
        bodyBuilder.part("application", "path/application");
        bodyBuilder.part("summary", "path/summary");
        bodyBuilder.part("date", "path/date");
        bodyBuilder.part("text", "path/text");
        bodyBuilder.part("title", "path/title");
        this.webTestClient.post().uri("/api/patent")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
     void uploadReturns400IfNotValidRequest() {
        when(service.uploadPatent(any(FilePart.class), any(PatentFieldsPaths.class), any(Boolean.class))).thenReturn(Mono.just(true).then());
        var bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", new ClassPathResource("US06193294B1.xml"));
        bodyBuilder.part("application", "");
        bodyBuilder.part("summary", "");
        bodyBuilder.part("date", "");
        bodyBuilder.part("text", "");
        bodyBuilder.part("title", "");
        this.webTestClient.post().uri("/api/patent")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ErrorResponse.class)
                .isEqualTo(new ErrorResponse(ApplicationError.INVALID_XML_PATHS.getCode(), ApplicationError.INVALID_XML_PATHS.getMessage()));
    }

    @Test
     void getReturnsPatent() {
        when(service.uploadPatent(any(FilePart.class), any(PatentFieldsPaths.class), any(Boolean.class))).thenReturn(Mono.just(true).then());
        List<Patent> patentBody = Collections.singletonList(new Patent("", Date.from(Instant.now()),"","",""));
        when(service.findPatent(patentBody.get(0).getUuid())).thenReturn(Flux.fromIterable(patentBody));
        this.webTestClient.get()
                .uri("/api/patent/" + patentBody.get(0).getUuid().toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().jsonPath(".uuid").isEqualTo(patentBody.get(0).getUuid().toString());
        verify(service).findPatent(any(UUID.class));
    }

    @Test
     void getReturnsPatentWithApplicationReference() {
        Patent patentBody = new Patent("app-ref", Date.from(Instant.now()),"","","");
        when(service.findPatent(any(String.class))).thenReturn(Flux.just(patentBody));
        this.webTestClient.get()
                .uri("/api/patent/app-ref")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().jsonPath(".uuid").isEqualTo(patentBody.getUuid().toString());
        verify(service).findPatent(any(String.class));
    }

}