package com.project.order.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.DTO.order.OrdersDTOSend;

import org.springframework.kafka.core.KafkaTemplate;

@Service
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // public void sendOrderRequest(String message) {
    //     kafkaTemplate.send("order-request-topic", message);
    // }

    public void sendOrderRequest(OrdersDTOSend message) {
        kafkaTemplate.send("order-topic2", message);
    }
}
