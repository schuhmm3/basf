package com.basf.patentmanager.infrastructure.repository;

import com.basf.patentmanager.domain.model.Patent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MongoRepositoryIntegrationTest {

    private final static Patent TEST_PATENT = new Patent("test", Date.from(Instant.now()), "test", "test", "test");

    @Autowired
    private MongoRepository mongoRepository;

    @Test
    void findById() {
        Patent presavedPatent = this.mongoRepository.save(TEST_PATENT).block();
        assumeTrue(presavedPatent != null, () -> "Patent could not be pre-saved to run the test");
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
}