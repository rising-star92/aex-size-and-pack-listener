package com.walmart.aex.sizeandpack.listener.service;

import com.walmart.aex.sizeandpack.listener.dto.quote.PlmAcceptedQuoteFineline;
import com.walmart.aex.sizeandpack.listener.exception.ClpApListenerException;
import com.walmart.aex.sizeandpack.listener.properties.PlmApiProperties;
import io.strati.ccm.utils.client.annotation.ManagedConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.walmart.aex.sizeandpack.listener.utils.CommonUtils.getHttpHeaders;

@Service
@Slf4j
public class PlmServiceCall {
    public static final String ERROR_GETTING_DATA_FROM_PLM_SERVICE = "Error getting Data from PLM service: ";
    @Autowired
    private RestTemplate restTemplate;

    @ManagedConfiguration
    public PlmApiProperties plmApiProperties;

    @Retryable(exclude = HttpClientErrorException.BadRequest.class, backoff = @Backoff(delay = 1000))
    public List<PlmAcceptedQuoteFineline> getApprovedQuoteFromPlm(Long planId, HttpMethod httpMethod) {
        List<PlmAcceptedQuoteFineline> responseMsg = null;
        try {
            HttpHeaders headers = getHttpHeaders(plmApiProperties.getPlmConsumerId(),
                    plmApiProperties.getPlmAppKey(), plmApiProperties.getPlmEnv());
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            log.info("Calling AEX-AEX_PLM_SERVICES API to retrieve approvedQuotes : {}", httpMethod.name());
            ResponseEntity<List<PlmAcceptedQuoteFineline>> responseEntity = restTemplate.exchange(
                    plmApiProperties.getPlmApiBaseURL().concat("/approvedQuotes?planId={planId}"),
                    httpMethod,
                    entity, new ParameterizedTypeReference<>() {
                    }, planId);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (HttpStatus.OK == statusCode) {
                responseMsg = responseEntity.getBody();
                log.info("Retrieved approvedQuote Successfully: {}", responseEntity.getBody());
            } else {
                log.info(ERROR_GETTING_DATA_FROM_PLM_SERVICE + responseEntity.getBody());
                throw new ClpApListenerException(ERROR_GETTING_DATA_FROM_PLM_SERVICE + planId);
            }
        } catch (Exception e) {
            log.error("Exception while getting PLM data::{} Exception: ", planId, e);
            throw new ClpApListenerException(ERROR_GETTING_DATA_FROM_PLM_SERVICE + planId);
        }
        return responseMsg;
    }

    @Recover
    public String recover(Exception e, Long planId, HttpMethod httpMethod) {
        throw new ClpApListenerException(ERROR_GETTING_DATA_FROM_PLM_SERVICE + planId, e);
    }

}
