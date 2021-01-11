package com.basf.nlpprocessor.application.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Request model to process a document
 *
 * @author robertogomez
 */
@Data
public class ProcessRequest {

    /**
     * Text to process in the NLP Pipeline
     */
    @Schema(description = "Text to process with the NLP Pipeline")
    @NotBlank
    private String text;
}
