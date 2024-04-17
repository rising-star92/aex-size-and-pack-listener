package com.walmart.aex.sizeandpack.listener.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.aex.sizeandpack.listener.dto.CLPMessageDTO;
import com.walmart.aex.sizeandpack.listener.dto.ChangeScopeDTO;
import com.walmart.aex.sizeandpack.listener.dto.HeaderDTO;
import com.walmart.aex.sizeandpack.listener.properties.KafkaProperties;
import io.strati.ccm.utils.client.annotation.ManagedConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Component
@Slf4j
public class ListenerMessageUtil {

    @ManagedConfiguration
    private KafkaProperties kafkaProperties;

    public boolean isMessageNotEligibleForSP(ConsumerRecord<String, String> consumerRecord) {
        if(consumerRecord.value().contains("PLAN_DEFINITION_CREATE")){
            return false;
        }else if(Boolean.parseBoolean(kafkaProperties.getKafkaConsumerInitialLoadFlag())){
            return false;
        }
        else {
            ObjectMapper mapper = new ObjectMapper();
            try {
                CLPMessageDTO message = mapper.readValue(consumerRecord.value(), CLPMessageDTO.class);
                log.info("message: {}", message.toString());
                return !(isTypeAccepted(message)
                        && isUpdateAccepted(message));
            } catch (Exception ex) {
                log.error("Exception encountered while processing message ::{} : ex :: {}", consumerRecord.value(), ex);
                return true;
            }
        }
    }

    private boolean isUpdateAccepted(CLPMessageDTO message) {
        ChangeScopeDTO changeScopeDTO = Optional.ofNullable(message.getHeaders())
                .map(HeaderDTO::getChangeScope)
                .orElse(new ChangeScopeDTO());

        return CollectionUtils.isEmpty(changeScopeDTO.getUpdatedAttributes()) || kafkaProperties.getKafkaConsumerAcceptedUpdates().stream()
                .anyMatch(changeScopeDTO.getUpdatedAttributes()::contains);
    }

    private boolean isTypeAccepted(CLPMessageDTO message) {
        return !CollectionUtils.isEmpty(kafkaProperties.getKafkaConsumerAcceptedTypes())
                && message.getHeaders() != null
                && kafkaProperties.getKafkaConsumerAcceptedTypes().stream()
                .anyMatch(message.getHeaders().getType()::equalsIgnoreCase);
    }
}
