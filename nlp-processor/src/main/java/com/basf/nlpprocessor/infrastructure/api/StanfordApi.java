package com.basf.nlpprocessor.infrastructure.api;

import com.basf.nlpprocessor.domain.api.NlpApi;
import com.basf.nlpprocessor.domain.model.Document;
import com.basf.nlpprocessor.domain.model.Entity;
import com.basf.nlpprocessor.domain.model.Token;
import com.basf.nlpprocessor.infrastructure.config.StanfordProperties;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Interfaces that implements {@link NlpApi} to run the NLP process using {@link StanfordCoreNLP}
 *
 * @author robertogomez
 */
@Component
public class StanfordApi implements NlpApi {

    private final StanfordCoreNLP pipeline;

    @Autowired
    public StanfordApi(StanfordProperties stanfordProperties) {

        Properties props = new Properties();
        props.setProperty("annotators", stanfordProperties.getComponents());
        props.setProperty("ner.applyFineGrained", String.valueOf(stanfordProperties.getNer().isApplyFineGrained()));
        props.setProperty("ner.useSUTime", String.valueOf(stanfordProperties.getNer().isUseSUTime()));
        props.setProperty("ner.applyNumericClassifiers", String.valueOf(stanfordProperties.getNer().isApplyNumericClassifiers()));

        // set up pipeline
        this.pipeline = new StanfordCoreNLP(props);
    }

    /**
     * Process the text with an NLP Pipeline
     *
     * @param text Text to process
     * @return {@link Mono} emitting the {@link Document} with the result of the NLP process or {@link Mono#empty()} if none.
     */
    public Mono<Document> processNlp(String text) {
        if (text != null && !text.isEmpty()) {
            CoreDocument doc = new CoreDocument(text);
            this.pipeline.annotate(doc);
            return Mono.just(new Document(
                    doc.tokens()
                            .parallelStream()
                            .map(t -> new Token(t.originalText(), t.tag(), t.lemma(), !t.ner().equals("O") ? t.ner() : ""))
                            .collect(Collectors.toList()),
                    text,
                    doc.entityMentions().parallelStream()
                            .map(ent -> new Entity(ent.entityType(), ent.text()))
                            .collect(Collectors.toList())));
        }else {
            return Mono.empty();
        }
    }

}
