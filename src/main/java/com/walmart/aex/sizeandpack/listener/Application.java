package com.walmart.aex.sizeandpack.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@ComponentScan("com.walmart.platform.txn.springboot.filters")
@ComponentScan("com.walmart.platform.txn.springboot.interceptor")
@SpringBootApplication(scanBasePackages = {"com.walmart.aex.sizeandpack.listener",
        "io.strati.tunr.utils.client"}, exclude = {KafkaAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableRetry
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
