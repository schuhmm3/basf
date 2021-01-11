package com.basf.nlpprocessor.application.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NlpException extends RuntimeException{

    private final int errorCode;

    public NlpException(ApplicationError applicationError) {
        super(applicationError.getMessage());
        this.errorCode = applicationError.getCode();
    }

    public NlpException(ApplicationError applicationError, Throwable cause) {
        super(applicationError.getMessage(), cause);
        this.errorCode = applicationError.getCode();
    }
}
