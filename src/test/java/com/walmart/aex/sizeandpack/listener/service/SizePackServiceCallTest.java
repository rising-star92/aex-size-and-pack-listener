package com.walmart.aex.sizeandpack.listener.service;

import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request.SizeAndPackDeletePayloadDTO;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request.SizeAndPackPayloadDTO;
import com.walmart.aex.sizeandpack.listener.exception.ClpApListenerException;
import com.walmart.aex.sizeandpack.listener.properties.SizeandPackServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class SizePackServiceCallTest {

    @InjectMocks
    private SizeandPackServiceCall sizeandPackServiceCall;

    @Mock
    private SizeandPackServiceProperties sizeandPackServiceProperties;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() throws IllegalAccessException {
        Field field2 = ReflectionUtils.findField(SizeandPackServiceCall.class, "sizeandPackServiceProperties");
        field2.setAccessible(true);
        field2.set(sizeandPackServiceCall, sizeandPackServiceProperties);

        Mockito.when(sizeandPackServiceProperties.getSizeAndPackApiBaseURL()).thenReturn("testBaseUrl");
    }
    @Test
    public void testPostStrategyCreatePayloadSuccess() throws IOException {
        SizeAndPackPayloadDTO sizeAndPackPayloadDTO = new SizeAndPackPayloadDTO();
        ResponseEntity entity = new ResponseEntity(HttpStatus.CREATED);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(HttpEntity.class), Mockito.<Class<String>>any())).thenReturn(entity);

        sizeandPackServiceCall.postEventSizePackService(sizeAndPackPayloadDTO, HttpMethod.POST);
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.anyString(), Mockito.any(),Mockito.any(HttpEntity.class), (Class<Object>) Mockito.any());
    }

    @Test
    public void testDeleteStrategyCreatePayloadSuccess() throws IOException {
        SizeAndPackDeletePayloadDTO sizeAndPackDeletePayloadDTO = new SizeAndPackDeletePayloadDTO();
        ResponseEntity entity = new ResponseEntity(HttpStatus.CREATED);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(HttpEntity.class), Mockito.<Class<String>>any())).thenReturn(entity);

        sizeandPackServiceCall.deleteEventSizePackService(sizeAndPackDeletePayloadDTO, HttpMethod.DELETE);
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.anyString(), Mockito.any(),Mockito.any(HttpEntity.class), (Class<Object>) Mockito.any());
    }

    @Test(expected = ClpApListenerException.class)
    public void test_postEventSizePackServiceShouldReturnExceptionWhenHttpStatusIsNot200Or201() {
        SizeAndPackPayloadDTO sizeAndPackPayloadDTO = new SizeAndPackPayloadDTO();
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(HttpEntity.class), Mockito.<Class<String>>any())).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        sizeandPackServiceCall.postEventSizePackService(sizeAndPackPayloadDTO, HttpMethod.POST);
    }

    @Test(expected = ClpApListenerException.class)
    public void test_deleteEventSizePackServiceShouldReturnExceptionWhenHttpStatusIsNot200Or201() {
        SizeAndPackDeletePayloadDTO sizeAndPackDeletePayloadDTO = new SizeAndPackDeletePayloadDTO();
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(HttpEntity.class), Mockito.<Class<String>>any())).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        sizeandPackServiceCall.deleteEventSizePackService(sizeAndPackDeletePayloadDTO, HttpMethod.DELETE);
    }

    @Test(expected = ClpApListenerException.class)
    public void test_recoverShouldThrowException(){
        SizeAndPackPayloadDTO sizeAndPackPayloadDTO = new SizeAndPackPayloadDTO();
        sizeandPackServiceCall.recover(new RuntimeException(), sizeAndPackPayloadDTO, HttpMethod.POST);
    }
}
