package com.walmart.aex.sizeandpack.listener.properties;

import io.strati.ccm.utils.client.annotation.Configuration;
import io.strati.ccm.utils.client.annotation.Property;

@Configuration(configName = "sizepackConfig")
public interface SizeandPackServiceProperties {

	@Property(propertyName = "sizepack.baseUrl")
    String getSizeAndPackApiBaseURL();

    @Property(propertyName = "sizepack.consumerId")
    String getSizeAndPackConsumerId();

    @Property(propertyName = "sizepack.appKey")
    String getSizeAndPackAppKey();

    @Property(propertyName = "sizepack.env")
    String getSizeAndPackEnv();
}
