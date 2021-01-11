package com.basf.patentmanager.application.model.dto;

import com.googlecode.jmapper.annotations.JMap;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotBlank;

/**
 * DTO to store the paths to to retrieve each field from the XML
 *
 * @author robertogomez
 */
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

    /**
     * Checks if all the paths are not empty or null.
     *
     * @return True if paths are not null or empty, false otherwise.
     */
    public boolean pathsAreValid() {
        return this.application != null && !this.application.isEmpty() &&
                this.date != null && !this.date.isEmpty() &&
                this.title != null && !this.title.isEmpty() &&
                this.summary != null && !this.summary.isEmpty() &&
                this.text != null && !this.text.isEmpty();
    }

}
