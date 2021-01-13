package com.basf.patentmanager.domain.model;

import com.googlecode.jmapper.annotations.JMap;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Patent entity that stores all the patent information
 *
 * @author robertogomez
 */
@Data
//Required annotations to use JMapper
@NoArgsConstructor
@RequiredArgsConstructor
public class Patent {

    @JMap
    private UUID uuid = UUID.randomUUID();

    @JMap
    @NonNull
    private String application;

    @JMap
    @NonNull
    private Date date;

    @JMap
    @NonNull
    private String title;

    @JMap
    @NonNull
    private String summary;

    @JMap
    @NonNull
    private String text;

    @JMap
    private List<Entity> entities;
}


