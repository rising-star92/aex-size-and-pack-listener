package com.walmart.aex.sizeandpack.listener.utils;

import com.walmart.aex.sizeandpack.listener.properties.KafkaProperties;
import com.walmart.aex.sizeandpack.listener.utils.ListenerMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class ListenerMessageUtilTest {
    @InjectMocks
    ListenerMessageUtil listenerMessageUtil;

    @Mock
    KafkaProperties kafkaProperties;

    @Before
    public void setUp() throws IllegalAccessException {
        Field field2 = ReflectionUtils.findField(ListenerMessageUtil.class, "kafkaProperties");
        field2.setAccessible(true);
        field2.set(listenerMessageUtil, kafkaProperties);

        List<String> types = new ArrayList<>();
        types.add("create");
        types.add("update");
        types.add("delete");

        List<String> channels = new ArrayList<>();
        channels.add("online");
        channels.add("store");
        channels.add("omni");

        List<String> updates = new ArrayList<>();
        updates.add("channel");

        Mockito.when(kafkaProperties.getKafkaConsumerAcceptedTypes()).thenReturn(types);
        Mockito.when(kafkaProperties.getKafkaConsumerAcceptedChannels()).thenReturn(channels);
        Mockito.when(kafkaProperties.getKafkaConsumerAcceptedUpdates()).thenReturn(updates);
    }

    @Test
    public void isMessageNotEligibleForSPTrueTest() throws IOException {
        String message = readTextFileAsString("clpMessageCreate");
        ConsumerRecord<String, String> record = new ConsumerRecord<String, String>("topic", 1, 0, 0L, TimestampType.CREATE_TIME, 0L, 0, 0,"1", message);
        assertFalse(listenerMessageUtil.isMessageNotEligibleForSP(record));
    }

    @Test
    public void isMessageNotEligibleForSPFalseTest() throws IOException {
        String message = readTextFileAsString("clpMessageCreateFalse");
        ConsumerRecord<String, String> record = new ConsumerRecord<String, String>("topic", 1, 0, 0L, TimestampType.CREATE_TIME, 0L, 0, 0,"1", message);
        assertTrue(listenerMessageUtil.isMessageNotEligibleForSP(record));
    }

    @Test
    public void isMessageNotEligibleForSPFalseTest2() throws IOException {
        ConsumerRecord<String, String> record = new ConsumerRecord<String, String>("topic", 1, 0, 0L, TimestampType.CREATE_TIME, 0L, 0, 0,"1", "exceptionText");
        assertTrue(listenerMessageUtil.isMessageNotEligibleForSP(record));
    }

    private String readTextFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName + ".txt")));
    }
}
