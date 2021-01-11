package com.basf.patentmanager.infrastructure.entity;

import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

/**
 * Patent entity that acts as the MongoDB Dto of the {@link com.basf.patentmanager.domain.model.Patent} domain entity
 *
 * @author robertogomez
 */
@Document(collection = "patent")
@Data
public class MongoPatentEntity {

    @JMap
    @Id
    private UUID uuid;

    @JMap
    private String application;

    @JMap
    private Date date;

    @JMap
    private String title;

    @JMap
    private String summary;

    @JMap
    private String text;

}
