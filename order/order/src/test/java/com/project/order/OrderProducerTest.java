package com.project.order;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.DTO.order.OrdersDTOSend;
import com.project.order.kafka.OrderProducer;

public class OrderProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private OrderProducer orderProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendOrderRequest() {
        OrdersDTOSend message = new OrdersDTOSend();
        orderProducer.sendOrderRequest(message);

        verify(kafkaTemplate).send("order-topic2", message);
    }
}