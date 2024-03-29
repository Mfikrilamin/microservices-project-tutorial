package com.microservicesproject.notificationservice.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import com.microservicesproject.notificationservice.event.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ApacheKafka {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final ObservationRegistry observationRegistry;
    private final Tracer tracer;

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // TODO : Implement an email service notification
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            LOGGER.info("Got message <{}>", orderPlacedEvent);
            LOGGER.info("TraceId- {}, Received Notification for Order - {}",
                    this.tracer.currentSpan().context().traceId(),
                    orderPlacedEvent.getOrderNumber());
        });
    }
}
