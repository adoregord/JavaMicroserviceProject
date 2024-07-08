package com.example.orchestrator.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.DTO.order.OrdersDTOSend;
import com.example.orchestrator.OrchestratorService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrchestratorConsumer {

    @Autowired
    private OrchestratorProducer orchestratorProducer;

    @Autowired
    private OrchestratorService orchestratorService;
    
    @KafkaListener(topics = "order-topic2", groupId = "didiKerenGroup")
    public void consumeOrderResponse(OrdersDTOSend message) {
        log.info(message.toString() + "send this object to project service via WebClient");
        orchestratorService.checkAndDeductStock(message).subscribe();
        // orchestratorProducer.sendToProductToCheck(message);
    }

    @KafkaListener(topics = "update-order-orchestrator", groupId = "didiKerenGroup")
    public void consumeProductResponseToUpdate(OrdersDTOSend message) {
        log.info(message.toString() + " sending this object to order service");
        orchestratorProducer.sendToOrderUpdate(message);
    }

    @KafkaListener(topics = "update-order-orchestrator-fail", groupId = "didiKerenGroup")
    public void consumeProductResponseFail(OrdersDTOSend message) {
        log.info(message.toString() + " sending this object to order service to set order status to FAIL");
        orchestratorProducer.sendToOrderUpdateFail(message);
    }

}
