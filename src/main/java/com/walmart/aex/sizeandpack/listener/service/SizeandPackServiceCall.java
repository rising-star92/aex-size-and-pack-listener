package com.walmart.aex.sizeandpack.listener.service;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request.SizeAndPackDeletePayloadDTO;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request.SizeAndPackPayloadDTO;
import com.walmart.aex.sizeandpack.listener.exception.ClpApListenerException;
import com.walmart.aex.sizeandpack.listener.properties.SizeandPackServiceProperties;
import io.strati.ccm.utils.client.annotation.ManagedConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.walmart.aex.sizeandpack.listener.utils.CommonUtils.getHttpHeaders;

@Service
@Slf4j
public class SizeandPackServiceCall {

	@Autowired
    RestTemplate restTemplate;

    @ManagedConfiguration
    public SizeandPackServiceProperties sizeandPackServiceProperties;

    @Retryable(exclude = HttpClientErrorException.BadRequest.class, backoff = @Backoff(delay = 10000))
    public String postEventSizePackService(SizeAndPackPayloadDTO sizeAndPackPayloadDTO, HttpMethod httpMethod) {
        String responseMsg = null;
        try {
            HttpHeaders headers = getHttpHeaders(sizeandPackServiceProperties.getSizeAndPackConsumerId(),
                    sizeandPackServiceProperties.getSizeAndPackAppKey(),
                    sizeandPackServiceProperties.getSizeAndPackEnv());
            HttpEntity<SizeAndPackPayloadDTO> entity = new HttpEntity<>(sizeAndPackPayloadDTO,headers);
            log.info("Calling AEX-SIZEANDPACK-SERVICE API for Insert Event: {}", httpMethod.name());
            ResponseEntity<String> responseEntity = restTemplate.exchange(sizeandPackServiceProperties.getSizeAndPackApiBaseURL().concat("/size-and-pack/v1/sizeAndPackService"), httpMethod, entity, String.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (HttpStatus.CREATED == statusCode || HttpStatus.OK == statusCode) {
                responseMsg = responseEntity.getBody();
                log.info("Create Event Posted Successfully: {}", responseEntity.getBody());
            } else {
                log.info("Error posting Create Event: {}", responseEntity.getBody());
                throw new ClpApListenerException("Error posting event to SizeAndPack service: "+ sizeAndPackPayloadDTO);
            }
        } catch (Exception e) {
            log.error("Exception while posting Create Event::{} Exception: ", sizeAndPackPayloadDTO, e);
            throw new ClpApListenerException("Error posting event to SizeAndPack service: "+ sizeAndPackPayloadDTO);
        }
        return responseMsg;
    }

    @Recover
    public String recover(Exception e, SizeAndPackPayloadDTO sizeAndPackPayloadDTO, HttpMethod httpMethod) {
        throw new ClpApListenerException("TimeOut posting event to SizeAndPack service: " + sizeAndPackPayloadDTO, e);
    }

    @Retryable(exclude = HttpClientErrorException.BadRequest.class, backoff = @Backoff(delay = 10000))
    public String deleteEventSizePackService(SizeAndPackDeletePayloadDTO strategyDeletePayloadDTO, HttpMethod delete) {
        String responseMsg = null;
        try {
            HttpHeaders headers = getHttpHeaders(sizeandPackServiceProperties.getSizeAndPackConsumerId(),
                    sizeandPackServiceProperties.getSizeAndPackAppKey(),
                    sizeandPackServiceProperties.getSizeAndPackEnv());
            HttpEntity<SizeAndPackDeletePayloadDTO> entity = new HttpEntity<>(strategyDeletePayloadDTO,headers);
            log.info("Calling AEX-SIZEANDPACK-SERVICE API method : {} ", delete.name());
            ResponseEntity<String> responseEntity = restTemplate.exchange(sizeandPackServiceProperties.getSizeAndPackApiBaseURL().concat("/size-and-pack/v1/sizeAndPackService"),
                    delete, entity, String.class);
            responseMsg = responseEntity.getBody();
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (HttpStatus.CREATED == statusCode || HttpStatus.OK == statusCode) {
                log.info("Delete Event Posted Successfully: {}", responseEntity.getBody());
            } else {
                log.info("Error posting Delete Event: {}", responseEntity.getBody());
                throw new ClpApListenerException("Error posting delete event to SizeAndPack service: "+ strategyDeletePayloadDTO);
            }
        } catch (Exception e) {
            log.error("Exception while posting Delete Event::{} Exception: ", strategyDeletePayloadDTO, e);
            throw new ClpApListenerException("Error posting delete event to SizeAndPack service: "+ strategyDeletePayloadDTO);
        }
        return responseMsg;
    }
}
