package com.walmart.aex.sizeandpack.listener.config;

import com.walmart.aex.sizeandpack.listener.properties.KafkaProperties;
import com.walmart.aex.sizeandpack.listener.properties.SecretProperties;
import com.walmart.aex.sizeandpack.listener.utils.ListenerMessageUtil;
import io.strati.ccm.utils.client.annotation.ManagedConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Configuration
@Slf4j
@EnableKafka
public class KafkaConfig {

    private static final String TMP_KAFKATRUSTSTORE_JKS = "/tmp/kafkatruststore.jks";
    private static final String ETC_SECRETS_SSL_TRUSTSTORE_TXT = "/etc/secrets/ssl.truststore.txt";
    private static final String TMP_KAFKAKEYSTORE_JKS = "/tmp/kafkakeystore.jks";
    private static final String ETC_SECRETS_SSL_KEYSTORE_TXT = "/etc/secrets/ssl.keystore.txt";
    @ManagedConfiguration
    private KafkaProperties kafkaProperties;

    @Autowired
    private ListenerMessageUtil listenerMessageUtil;

    private SecretProperties secretProperties;

    public KafkaConfig(SecretProperties secretProperties) {
        this.secretProperties = secretProperties;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getListenerKafkaServer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getListenerGroupId());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getSessionTimeoutConfig());
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaProperties.getHeartbeatIntervalConfig());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getAutoOffsetResetConfig());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getEnableAutoCommitConfig());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", getTrustStoreFileLocation());
        props.put("ssl.truststore.password", secretProperties.getTrustStoreFilePassword());
        props.put("ssl.keystore.location", getKeyStoreFileLocation());
        props.put("ssl.keystore.password", secretProperties.getTrustStoreFilePassword());
        props.put("ssl.key.password", secretProperties.getTrustStoreFilePassword());
        props.put("ssl.endpoint.identification.algorithm", "");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(this::isMessageNotEligibleForSP);
        return factory;
    }

    private boolean isMessageNotEligibleForSP(ConsumerRecord<String, String> consumerRecord) {
        return listenerMessageUtil.isMessageNotEligibleForSP(consumerRecord);
    }

    private String getTrustStoreFileLocation() {
        String truststoreFileName = TMP_KAFKATRUSTSTORE_JKS;

        File file = new File(truststoreFileName);
        log.info("Truststore File Location: {}", truststoreFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            String truststore = new String(Files.readAllBytes(Paths.get(ETC_SECRETS_SSL_TRUSTSTORE_TXT)));
            fos.write(Base64.getDecoder().decode(truststore));
        } catch (Exception e) {
            log.error(
                    "error writing file: {} | {} | {}",
                    e.getClass().getCanonicalName(),
                    e.getMessage(),
                    e.getCause());
            return "";
        }
        return truststoreFileName;
    }

    private String getKeyStoreFileLocation() {
        String keystoreFileName = TMP_KAFKAKEYSTORE_JKS;

        File file = new File(keystoreFileName);
        log.info("Truststore File Location: {}", keystoreFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            String keystore = new String(Files.readAllBytes(Paths.get(ETC_SECRETS_SSL_KEYSTORE_TXT)));
            fos.write(Base64.getDecoder().decode(keystore));
        } catch (Exception e) {
            log.error(
                    "error writing file: {} | {} | {}",
                    e.getClass().getCanonicalName(),
                    e.getMessage(),
                    e.getCause());
            return "";
        }
        return keystoreFileName;
    }
}
