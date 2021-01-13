package com.basf.patentmanager.infrastructure.repository;

import com.basf.patentmanager.domain.model.Patent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MongoRepositoryIntegrationTest {

    private final static Patent TEST_PATENT = new Patent("test", Date.from(Instant.now()), "test", "test", "test");
    private final static Patent TEST_PATENT_2 = new Patent("test_2", Date.from(Instant.now()), "test_2", "test_2", "test_2");

    @Autowired
    private MongoRepository mongoRepository;

    @Test
    void findById() {
        Patent preSavedPatent = this.mongoRepository.save(TEST_PATENT).block();
        assumeTrue(preSavedPatent != null, () -> "Patent could not be pre-saved to run the test");
        Patent patent = this.mongoRepository.findById(TEST_PATENT.getUuid()).block();
        assertNotNull(patent, "Patent cannot be null");
        assertEquals(TEST_PATENT, patent, "Patent should be equals to the test patent");
    }

    @Test
    void save() {
        Patent patent = this.mongoRepository.save(TEST_PATENT).block();
        assertNotNull(patent, "Patent cannot be null");
        assertEquals(TEST_PATENT, patent, "Patent should be equals to the test patent");
    }

    @Test
    void findByApplication() {
        Patent preSavedPatent = this.mongoRepository.save(TEST_PATENT).block();
        assumeTrue(preSavedPatent != null, () -> "Patent could not be pre-saved to run the test");
        List<Patent> patent = this.mongoRepository.findByApplication(TEST_PATENT.getApplication()).toStream().collect(Collectors.toList());
        assertNotNull(patent, "Patent cannot be null");
        assertEquals(1, patent.size(), "Patent cannot be null");
        assertEquals(TEST_PATENT, patent.get(0), "Patent should be equals to the test patent");
    }

    @Test
    void deleteById() {
        Patent preSavedPatent = this.mongoRepository.save(TEST_PATENT).block();
        Patent preSavedPatent2 = this.mongoRepository.save(TEST_PATENT_2).block();
        assumeTrue(preSavedPatent != null && preSavedPatent2 != null, () -> "Patents could not be pre-saved to run the test");
        this.mongoRepository.deleteById(TEST_PATENT.getUuid()).block();
        Patent patent = this.mongoRepository.findById(TEST_PATENT_2.getUuid()).block();
        assertNotNull(patent, "Patent cannot be null");
        assertEquals(TEST_PATENT_2, patent, "Patent should be equals to the test patent");
        patent = this.mongoRepository.findById(TEST_PATENT.getUuid()).block();
        assertNull(patent, "Patent needs to be null");
    }

    @Test
    void deleteAll() {
        Patent preSavedPatent = this.mongoRepository.save(TEST_PATENT).block();
        Patent preSavedPatent2 = this.mongoRepository.save(TEST_PATENT_2).block();
        assumeTrue(preSavedPatent != null && preSavedPatent2 != null, () -> "Patents could not be pre-saved to run the test");
        this.mongoRepository.deleteAll().block();
        Patent patent = this.mongoRepository.findById(TEST_PATENT_2.getUuid()).block();
        assertNull(patent, "Patent needs to be null");
        patent = this.mongoRepository.findById(TEST_PATENT.getUuid()).block();
        assertNull(patent, "Patent needs to be null");
    }
}