package com.basf.patentmanager.application.model.rest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Data
public class ProcessRequest {

    @NotBlank
    private FilePart file;

}

