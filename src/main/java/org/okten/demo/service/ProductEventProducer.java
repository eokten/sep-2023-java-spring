package org.okten.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.okten.demo.dto.event.ProductCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventProducer {

    private final KafkaTemplate<Integer, ProductCreatedEvent> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String productCreatedEventsTopic;

    public void publishProductCreatedEvent(ProductCreatedEvent event) {
        log.info("Sending product created event: {}", event);
        kafkaTemplate.send(productCreatedEventsTopic, event);
    }
}
