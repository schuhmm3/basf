package com.basf.patentmanager.application.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Exception to handle the application errors
 *
 * @author robertogomez
 */
@Getter
@Setter
public class PatentException extends RuntimeException {

    /**
     * Attribute to store the error code defined on {@link ApplicationError} enum
     */
    private final int errorCode;

    public PatentException(ApplicationError applicationError) {
        super(applicationError.getMessage());
        this.errorCode = applicationError.getCode();
    }

    public PatentException(ApplicationError applicationError, Throwable cause) {
        super(applicationError.getMessage(), cause);
        this.errorCode = applicationError.getCode();
    }
}
