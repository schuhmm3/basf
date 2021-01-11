package com.basf.patentmanager.application.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Error {

    private final int code;
    private final String message;

}
