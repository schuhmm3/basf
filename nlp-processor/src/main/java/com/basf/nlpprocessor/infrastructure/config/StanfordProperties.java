package com.basf.nlpprocessor.infrastructure.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Basic properties to configure {@link edu.stanford.nlp.pipeline.StanfordCoreNLP}
 */
@Configuration
@ConfigurationProperties(prefix = "stanford")
@Data
@NoArgsConstructor
public class StanfordProperties {

    /**
     * Properties to configure the NER
     */
    private Ner ner;

    /**
     * Components to include in the pipeline
     */
    private String components = "tokenize,ssplit,pos,lemma,ner";

    @Data
    @NoArgsConstructor
    private static class Ner {

        /**
         * Check if apply fine-grained NER, setting this to True will slow down the process.
         * Check <a>https://stanfordnlp.github.io/CoreNLP/ner.html</a> for more information.
         */
        private boolean nerApplyFineGrained = false;

    }
}
