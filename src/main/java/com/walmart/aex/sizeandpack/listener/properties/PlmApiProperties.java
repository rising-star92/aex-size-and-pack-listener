package com.walmart.aex.sizeandpack.listener.properties;

import io.strati.ccm.utils.client.annotation.Configuration;
import io.strati.ccm.utils.client.annotation.Property;

@Configuration(configName = "plmApiConfig")
public interface PlmApiProperties {
    @Property(propertyName = "plm.baseUrl")
    String getPlmApiBaseURL();
    @Property(propertyName = "plm.consumerId")
    String getPlmConsumerId();
    @Property(propertyName = "plm.appKey")
    String getPlmAppKey();
    @Property(propertyName = "plm.env")
    String getPlmEnv();
}
