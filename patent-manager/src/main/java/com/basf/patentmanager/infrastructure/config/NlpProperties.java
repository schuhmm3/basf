package com.basf.patentmanager.infrastructure.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties to configure an external NLP service
 *
 * @author robertogomez
 */
@Configuration
@ConfigurationProperties(prefix = "nlp")
@Data
@NoArgsConstructor
public class NlpProperties {

    /**
     * Protocol to call external nlp service
     */
    private String protocol;

    /**
     * External nlp service hostname
     */
    private String hostname;

    /**
     * External nlp service port
     */
    private int port;

    /**
     * External nlp service endpoint
     */
    private String nerEndpoint;

}
