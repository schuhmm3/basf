package com.basf.patentmanager.application.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotBlank;

/**
 * Request model to upload a file
 *
 * @author robertogomez
 */
@Data
public class UploadRequest {

    @NotBlank
    private FilePart file;

    @Schema(defaultValue = "questel-patent-document/bibliographic-data/application-reference/document-id[@data-format='questel']/doc-number")
    @NotBlank
    private String application;

    @Schema(defaultValue = "questel-patent-document/bibliographic-data/application-reference/document-id/date")
    @NotBlank
    private String date;

    @Schema(defaultValue = "questel-patent-document/bibliographic-data/invention-title")
    @NotBlank
    private String title;

    @Schema(defaultValue = "questel-patent-document/abstract")
    @NotBlank
    private String summary;

    @Schema(defaultValue = "questel-patent-document/description")
    @NotBlank
    private String text;

}
