package com.basf.nlpprocessor.infrastructure.api;

import com.basf.nlpprocessor.domain.api.NlpApi;
import com.basf.nlpprocessor.domain.model.Document;
import com.basf.nlpprocessor.domain.model.Entity;
import com.basf.nlpprocessor.infrastructure.config.NlpProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Asynchronous repository to save patents received through RabbitMQ or Kafka
 *
 * @author robertogomez
 */
@Component
@RequiredArgsConstructor
@Slf4j
class AsynchronousNlpApi {

    private final NlpApi nlpApi;
    private final NlpProperties nlpProperties;

    /**
     * Processes a {@link EntityEventInput} sent through Kafka or RabbitMQ
     *
     * @return {@link Function} accepting a {@link EntityEventInput} as input and producing a {@link Message<EntityEventOutput>} as output.
     */
    @Bean
    public Function<EntityEventInput, Message<EntityEventOutput>> processEntities() {
        return input -> {
            log.info("Received some input");
            return MessageBuilder
                    .withPayload(
                            new EntityEventOutput(
                                    input.getId(),
                                    this.nlpApi.processNlp(input.getText())
                                            .flatMapIterable(Document::getEntities).collectList().block())
                    )
                    .setHeader("spring.cloud.stream.sendto.destination", nlpProperties.getEntitiesDestination())
                    .build();
        };
    }

    /**
     * Dto to send the output message
     */
    @Data
    static class EntityEventOutput {
        private final UUID id;
        private final List<Entity> entities;
    }

    /**
     * Dto to receive the message
     */
    @Data
    static class EntityEventInput {
        private final UUID id;
        private final String text;
    }
}
