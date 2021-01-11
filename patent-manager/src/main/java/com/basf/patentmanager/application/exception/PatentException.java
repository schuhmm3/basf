package com.basf.patentmanager.application.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatentException extends RuntimeException{

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
