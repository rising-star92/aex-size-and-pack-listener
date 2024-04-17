package com.walmart.aex.sizeandpack.listener.properties;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Slf4j
public class SecretProperties {

    private final String LOCAL = "local";


    @Value("#{systemProperties['ssl.truststore.password']}")
    private String sslTrustStorePassword;

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Value("#{systemProperties['midasApi.authorization']}")
    private String midasAPIAuthorization;



    public Object getTrustStoreFilePassword() {
        String trustStorePassword = null;
        try {
            trustStorePassword = activeProfile.contains(LOCAL) ? sslTrustStorePassword :
                    new String(Files.readAllBytes(Paths.get("/etc/secrets/ssl.truststore.password.txt")));
        } catch (IOException e) {
            log.error("Error reading truststore password" + e.getMessage());
        }
        return trustStorePassword;
    }

    public String fetchMidasApiAuthorization() {
        String midasAuth = null;
        try {
            midasAuth = activeProfile.contains(LOCAL) ? midasAPIAuthorization : new String(Files.readAllBytes(Paths.get("/etc" +
                    "/secrets/midasApi.authorization.txt")));
        } catch (IOException e) {
            log.error("Error reading midas api authorization" + e.getMessage());
        }
        return midasAuth;
    }
}
