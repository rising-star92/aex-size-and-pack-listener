package com.walmart.aex.sizeandpack.listener.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.aex.sizeandpack.listener.dto.quote.PlmAcceptedQuoteFineline;
import com.walmart.aex.sizeandpack.listener.exception.ClpApListenerException;
import com.walmart.aex.sizeandpack.listener.properties.PlmApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static com.walmart.aex.sizeandpack.listener.utils.CommonUtilsTest.readFileAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class PlmServiceCallTest {
    @InjectMocks
    private PlmServiceCall plmServiceCall;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    PlmApiProperties plmApiProperties;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        Field field = ReflectionUtils.findField(PlmServiceCall.class, "plmApiProperties");
        field.setAccessible(true);
        field.set(plmServiceCall, plmApiProperties);
        when(plmApiProperties.getPlmApiBaseURL()).thenReturn("testBaseUrl");
    }

    @Test
    public void test_getApprovedQuoteFromPlmShouldReturnApprovedQuoteFromPlm() throws IOException {
        List<PlmAcceptedQuoteFineline> plmAcceptedQuoteFineline = objectMapper.readValue(readFileAsString("PlmResponse", ".json"), new TypeReference<>() {});
        ResponseEntity<List<PlmAcceptedQuoteFineline>> response = ResponseEntity.status(HttpStatus.OK).body(plmAcceptedQuoteFineline);
        doReturn(response).when(restTemplate).exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class), anyLong());
        List<PlmAcceptedQuoteFineline> actualApprovedQuoteFromPlm = plmServiceCall.getApprovedQuoteFromPlm(1L, HttpMethod.GET);
        assertEquals(15, actualApprovedQuoteFromPlm.size());
    }

    @Test(expected = ClpApListenerException.class)
    public void test_getApprovedQuoteFromPlmShouldThrowExceptionWhenHttpStatusIsNot200(){
        ResponseEntity<List<PlmAcceptedQuoteFineline>> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        doReturn(response).when(restTemplate).exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class), anyLong());
        plmServiceCall.getApprovedQuoteFromPlm(1L, HttpMethod.GET);
    }

    @Test(expected = ClpApListenerException.class)
    public void test_recoverShouldThrowException(){
        plmServiceCall.recover(new RuntimeException(), 1L, HttpMethod.GET);
    }


}
