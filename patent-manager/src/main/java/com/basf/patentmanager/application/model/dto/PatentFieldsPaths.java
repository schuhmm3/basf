package com.basf.patentmanager.application.model.dto;

import com.googlecode.jmapper.annotations.JMap;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotBlank;

@Data
public class PatentFieldsPaths {

    @JMap
    private String application;

    @JMap
    private String date;

    @JMap
    private String title;

    @JMap
    private String summary;

    @JMap
    private String text;

    public boolean pathsAreValid() {
        return this.application != null && !this.application.isEmpty() &&
                this.date != null && !this.date.isEmpty() &&
                this.title != null && !this.title.isEmpty() &&
                this.summary != null && !this.summary.isEmpty() &&
                this.text != null && !this.text.isEmpty();
    }

}
