package com.basf.patentmanager.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum class to handle all the errors in the application layer
 *
 * @author robertogomez
 */
@Getter
@AllArgsConstructor
public enum ApplicationError {

    INVALID_XML_PATHS(1001, "Paths from XML must not be empty"),
    INVALID_FILE_FORMAT(1002, "The application only can process ZIP files"),
    READ_FILE_ERROR(1003, "Error reading file"),
    PARSING_FILE_ERROR(1004, "Error parsing file"),
    EXTERNAL_NLP_SERVICE_ERROR(2001, "Error calling external NLP Service"),
    INTERNAL_ERROR(5001, "Internal error");

    /**
     * Error code
     */
    private final int code;
    /**
     * Message with the description of the error code
     */
    private final String message;

}
