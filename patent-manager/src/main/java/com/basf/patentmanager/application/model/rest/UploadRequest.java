package com.basf.patentmanager.application.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;
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
    @NonNull
    private FilePart file;

    @Schema(description = "Path to retrieve the application reference from the document", defaultValue = "questel-patent-document/bibliographic-data/application-reference/document-id[@data-format='questel']/doc-number")
    private String application = "questel-patent-document/bibliographic-data/application-reference/document-id[@data-format='questel']/doc-number";

    @Schema(description = "Path to retrieve the date from the document", defaultValue = "questel-patent-document/bibliographic-data/application-reference/document-id/date")
    private String date = "questel-patent-document/bibliographic-data/application-reference/document-id/date";

    @Schema(description = "Path to retrieve the title from the document", defaultValue = "questel-patent-document/bibliographic-data/invention-title")
    private String title = "questel-patent-document/bibliographic-data/invention-title";

    @Schema(name="abstract", description = "Path to retrieve the abstract from the document", defaultValue = "questel-patent-document/abstract")
    private String summary = "questel-patent-document/abstract";

    @Schema(description = "Path to retrieve the text from the document", defaultValue = "questel-patent-document/description")
    private String text = "questel-patent-document/description";

    @Schema(description = "Flag to run the process asynchronously", defaultValue = "true")
    private Boolean async = true;

}
