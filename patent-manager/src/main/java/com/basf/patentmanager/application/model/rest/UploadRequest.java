package com.basf.patentmanager.application.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotBlank;

@Data
public class UploadRequest {

    @NotBlank
    private FilePart file;

    @Schema(defaultValue = "questel-patent-document/bibliographic-data/application-reference/document-id[@data-format='questel']/doc-number")
    private String application;

    @Schema(defaultValue = "questel-patent-document/bibliographic-data/application-reference/document-id/date")
    private String date;

    @Schema(defaultValue = "questel-patent-document/bibliographic-data/invention-title")
    private String title;

    @Schema(defaultValue = "questel-patent-document/abstract")
    private String summary;

    @Schema(defaultValue = "questel-patent-document/description")
    private String text;

}
