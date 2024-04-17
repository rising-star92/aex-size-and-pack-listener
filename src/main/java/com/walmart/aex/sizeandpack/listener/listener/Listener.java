package com.walmart.aex.sizeandpack.listener.listener;

import com.walmart.aex.sizeandpack.listener.service.SPConsumerService;
import com.walmart.aex.sizeandpack.listener.exception.ClpApListenerException;
import com.walmart.aex.sizeandpack.listener.properties.KafkaProperties;
import io.strati.ccm.utils.client.annotation.ManagedConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Listener {

    @ManagedConfiguration
    public KafkaProperties kafkaProperties;

    @Autowired
    SPConsumerService spConsumerService;

    @KafkaListener(topics = "#{__listener.kafkaProperties.getListenerKafkaTopic()}",
            containerFactory = "kafkaListenerContainerFactory")
    public void apConsumer(@Payload String message, Acknowledgment ack) {
        try {
            log.info("Received message from CLP : {}", message);
            spConsumerService.processMessage(message);
            ack.acknowledge();
            log.info("Message processing has been completed for : {}", message);
        } catch (ClpApListenerException exception) {
            log.error("Exception processing message: {}", message, exception);
        } catch (Exception ex) {
            log.error("Internal Error processing message: {} ", message, ex);
        }
    }
}
