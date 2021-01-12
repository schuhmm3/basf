package com.basf.patentmanager.domain.service;

import com.basf.patentmanager.domain.api.NlpApi;
import com.basf.patentmanager.domain.model.Entity;
import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.repository.PatentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DomainPatentServiceTest {

    private final static Patent TEST_PATENT = new Patent("test", Date.from(Instant.now()), "test", "test", "test");
    private final static List<Entity> TEST_ENTITIES = Collections.singletonList(new Entity("ORGANIZATION","BASF"));
    private final PatentRepository patentRepository = mock(PatentRepository.class);
    private final NlpApi nlpApi = mock(NlpApi.class);
    private final DomainPatentService domainPatentService = new DomainPatentService(patentRepository, nlpApi);

    @Test
    void addPatent() {
        when(patentRepository.save(any(Patent.class))).thenAnswer(i -> Mono.just(i.getArguments()[0]));
        when(nlpApi.getEntities(any(String.class))).thenReturn(TEST_ENTITIES);
        Patent patent = domainPatentService.addPatent(TEST_PATENT).block();
        assertNotNull(patent);
        assertEquals(TEST_ENTITIES, patent.getEntities());
    }

}