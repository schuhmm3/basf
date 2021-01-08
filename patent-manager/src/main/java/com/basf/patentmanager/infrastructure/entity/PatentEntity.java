package com.basf.patentmanager.infrastructure.entity;

import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document
@Data
public class PatentEntity {

    @JMap
    @Id
    private UUID uuid;

    @JMap
    private String applicationNumber;

    @JMap
    private Date date;

    @JMap
    private String title;

    @JMap
    private String summary;

    @JMap
    private String text;

}
