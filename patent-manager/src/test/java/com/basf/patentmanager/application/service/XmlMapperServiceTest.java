package com.basf.patentmanager.application.service;

import com.basf.patentmanager.application.model.dto.PatentFieldsPaths;
import com.basf.patentmanager.domain.model.Patent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class XmlMapperServiceTest {

    private final XmlMapperService xmlMapperService = new XmlMapperService();

    @Test
    void createPatentFromXml() throws IOException {
        PatentFieldsPaths paths = new PatentFieldsPaths();
        paths.setApplication("questel-patent-document/bibliographic-data/application-reference/document-id[@data-format='questel']/doc-number");
        paths.setDate("questel-patent-document/bibliographic-data/application-reference/document-id/date");
        paths.setSummary("questel-patent-document/abstract");
        paths.setTitle("questel-patent-document/bibliographic-data/invention-title");
        paths.setText("questel-patent-document/description");
        Patent patent = this.xmlMapperService.createPatentFromXml(new ClassPathResource("US06193294B1.xml").getInputStream(), paths);
        assertFalse(patent.getApplication().isEmpty());
        assertFalse(patent.getText().isEmpty());
        assertFalse(patent.getTitle().isEmpty());
        assertFalse(patent.getSummary().isEmpty());
        assertNotNull(patent.getDate());
    }
}