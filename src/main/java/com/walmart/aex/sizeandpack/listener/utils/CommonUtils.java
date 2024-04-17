package com.walmart.aex.sizeandpack.listener.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@UtilityClass
public class CommonUtils {
    public static HttpHeaders getHttpHeaders(String consumerId, String name, String env) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "*/*");
        headers.set("WM_CONSUMER.ID", consumerId);
        headers.set("WM_SVC.NAME", name);
        headers.set("WM_SVC.ENV", env);
        return headers;
    }
}
