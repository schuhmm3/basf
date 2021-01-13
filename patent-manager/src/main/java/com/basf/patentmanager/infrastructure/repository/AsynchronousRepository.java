package com.basf.patentmanager.infrastructure.repository;

import com.basf.patentmanager.domain.model.Patent;
import com.basf.patentmanager.domain.repository.PatentRepository;
import com.basf.patentmanager.infrastructure.entity.EntityEventInput;
import com.basf.patentmanager.infrastructure.entity.EntityEventOutput;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Asynchronous repository to update patents with extra information received through Kafka or RabbitMQ
 *
 * @author robertogomez
 */
@Component
@Getter
@Slf4j
public class AsynchronousRepository {

    private final PatentRepository patentRepository;

    private final BlockingQueue<EntityEventInput> entityEventInputs;

    public AsynchronousRepository(PatentRepository patentRepository){
        this.patentRepository = patentRepository;
        this.entityEventInputs = new LinkedBlockingQueue<>();
    }

    /**
     * Sends the text and ID from a {@link Patent} as an {@link EntityEventInput} through Kafka or RabbitMQ
     */
    @Bean
    public Supplier<EntityEventInput> entities() {
        return entityEventInputs::poll;
    }

    /**
     * Updates a {@link Patent} when a {@link EntityEventOutput} is received through Kafka or RabbitMQ with its entities
     *
     * @return {@link Function} accepting a {@link Patent} as input and producing a {@link Mono<Void>} as output.
     */
    @Bean
    public Consumer<EntityEventOutput> patent() {
        return entityEventOutput -> this.patentRepository
                .findById(entityEventOutput.getId())
                .flatMap(patent -> {
                    patent.setEntities(entityEventOutput.getEntities());
                    return this.patentRepository.save(patent);
                })
                .subscribe();
    }
}
