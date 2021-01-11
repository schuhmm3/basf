package com.basf.patentmanager.application.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Error model for the REST endpoints
 *
 * @author robertogomez
 */
@Data
public class ErrorResponse {

    /**
     * Error code
     */
    private final int code;
    /**
     * Message with the description of the error code
     */
    private final String message;

}
