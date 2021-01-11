
package com.basf.nlpprocessor.application.exception;

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

    INTERNAL_ERROR(5000, "Internal error");

    /**
     * Error code
     */
    private final int code;
    /**
     * Message with the description of the error code
     */
    private final String message;

}
