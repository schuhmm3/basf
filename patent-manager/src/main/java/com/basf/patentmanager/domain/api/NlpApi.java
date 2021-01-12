package com.basf.patentmanager.domain.api;

import com.basf.patentmanager.domain.model.Entity;

import java.util.List;

/**
 * Interface that defines the API to perform NLP operations using external services
 *
 * @author robertogomez
 */
public interface NlpApi {

    List<Entity> getEntities(String text);
}
