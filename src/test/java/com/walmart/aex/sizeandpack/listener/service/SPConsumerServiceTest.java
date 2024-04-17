package com.walmart.aex.sizeandpack.listener.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.aex.sizeandpack.listener.dto.quote.PlmAcceptedQuoteFineline;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request.SizeAndPackPayloadDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static com.walmart.aex.sizeandpack.listener.utils.CommonUtilsTest.readFileAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class SPConsumerServiceTest {

    @InjectMocks
    private SPConsumerService spConsumerService;
    @InjectMocks
    private ObjectMapper objectMapper;
    @Mock
    private PlmServiceCall plmServiceCall;
    @Mock
    private SizeandPackServiceCall sizeandPackServiceCall;
    @Captor
    private ArgumentCaptor<SizeAndPackPayloadDTO> sizeAndPackPayloadDTOCaptor;
    
    @Before
    public void setUp() throws IllegalAccessException {
        Field field2 = ReflectionUtils.findField(SPConsumerService.class, "objectMapper");
        field2.setAccessible(true);
        field2.set(spConsumerService, new ObjectMapper());
    }
    @Test
    public void processMessageCreateTest() throws IOException {
    	try {
    		String messagCre = readFileAsString("clpMessageCreate", ".txt");
    		spConsumerService.processMessage(messagCre);
            verify(sizeandPackServiceCall, times(1)).postEventSizePackService(any(), any());
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
        
    }
    
    @Test
    public void processMessageUpdateTest() throws IOException {
    	try {
    		String messagUpd = readFileAsString("clpMessageUpdate", ".txt");
    		spConsumerService.processMessage(messagUpd);
            verify(sizeandPackServiceCall, times(1)).postEventSizePackService(any(), any());
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
        
    }
    @Test
    public void processMessageDeleteTest() throws IOException {
    	try {
    		String messagDel = readFileAsString("clpMessageDelete", ".txt");
    		spConsumerService.processMessage(messagDel);
            verify(sizeandPackServiceCall, times(1)).deleteEventSizePackService(any(), any());
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
        
    }

    @Test
    public void test_processMessageShouldUpdateFactoryIdForAllCCIdInSizeAndPackPayloadDTOIfDataIsAvailableInPlm() throws IOException {
        List<PlmAcceptedQuoteFineline> plmAcceptedQuoteFineline = objectMapper.readValue(readFileAsString("PlmResponse", ".json"), new TypeReference<>() {});
        when(plmServiceCall.getApprovedQuoteFromPlm(anyLong(), any())).thenReturn(plmAcceptedQuoteFineline);
        spConsumerService.processMessage(readFileAsString("LinePlanMessage", ".txt"));
        verify(sizeandPackServiceCall, times(1)).postEventSizePackService(sizeAndPackPayloadDTOCaptor.capture(), any());
        assertEquals(readFileAsString("SizeAndPackPayloadAfterFactoryIdUpdate", ".txt"), objectMapper.writeValueAsString(sizeAndPackPayloadDTOCaptor.getValue()));
    }

    @Test
    public void test_processMessageShouldLogExceptionWhenHeaderIsNotAvailable() throws IOException {
        String messagUpd = readFileAsString("clpMessage", ".json");
        spConsumerService.processMessage(messagUpd);
        verify(sizeandPackServiceCall, times(0)).postEventSizePackService(any(), any());
        verify(sizeandPackServiceCall, times(0)).deleteEventSizePackService(any(), any());
    }

}
