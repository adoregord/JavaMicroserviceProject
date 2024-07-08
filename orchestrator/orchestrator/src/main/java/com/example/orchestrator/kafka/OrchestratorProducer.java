package com.example.orchestrator.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DTO.order.OrdersDTOSend;

import org.springframework.kafka.core.KafkaTemplate;

@Service
public class OrchestratorProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // public void sendToProductToCheck(OrdersDTOSend message) {
    //     kafkaTemplate.send("check-product", message);
    // }

    public void sendToOrderUpdate(OrdersDTOSend message) {
        kafkaTemplate.send("order-update", message);
    }

    public void sendToOrderUpdateFail(OrdersDTOSend message) {
        kafkaTemplate.send("order-update-fail", message);
    }

}
