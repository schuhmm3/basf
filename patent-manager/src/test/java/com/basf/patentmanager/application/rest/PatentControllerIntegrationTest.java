package com.basf.patentmanager.application.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
class PatentControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void process() {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ClassPathResource("uspat1_201831_back_80001_100000.zip"))
                .contentType(MediaType.MULTIPART_FORM_DATA);
        webTestClient
                .post().uri("/api/patent")
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .accept(MediaType.MULTIPART_FORM_DATA)
                .exchange()
                // and use the dedicated DSL to test assertions against the response
                .expectStatus().isOk();
    }
}