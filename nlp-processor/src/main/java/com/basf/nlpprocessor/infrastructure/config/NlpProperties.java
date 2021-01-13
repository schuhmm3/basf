package com.basf.nlpprocessor.infrastructure.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Basic properties to configure the infrastructure
 */
@Configuration
@ConfigurationProperties(prefix = "nlp")
@Data
@NoArgsConstructor
public class NlpProperties {

    /**
     * Sets the output for the message when processing the entities asynchronously
     */
    private String entitiesDestination = "patent-input";

}
