package com.basf.patentmanager.domain.model;

import com.googlecode.jmapper.annotations.JMap;
import lombok.*;

import java.util.Date;
import java.util.UUID;

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
}


