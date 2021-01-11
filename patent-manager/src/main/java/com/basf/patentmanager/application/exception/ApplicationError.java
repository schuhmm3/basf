package com.basf.patentmanager.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationError {

    INVALID_XML_PATHS(1000, "Paths from XML must not be empty"),
    INTERNAL_ERROR(5000, "Internal error");

    private final int code;
    private final String message;

}
