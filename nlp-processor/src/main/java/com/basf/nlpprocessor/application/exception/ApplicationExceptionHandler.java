package com.basf.nlpprocessor.application.exception;

import com.basf.nlpprocessor.application.model.rest.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handlers in the application handler for the REST endpoints
 *
 * @author robertogomez
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {

    /**
     * Hnadler for all the {@link NlpException} and its children
     *
     * @param ex Exception to handle
     * @return Response entity with the {@link ErrorResponse} on its body
     */
    @ExceptionHandler(NlpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleNlpException(NlpException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getErrorCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
