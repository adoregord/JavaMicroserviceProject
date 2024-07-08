package com.project.order.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.DTO.order.OrdersDTOSend;
import com.project.order.orders.OrdersService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderConsumer {

    @Autowired
    private OrdersService ordersService;
    
    @KafkaListener(topics = "order-update", groupId = "didiKerenGroup")
    public void consumeOrderResponse(OrdersDTOSend message) {
        log.info("Product approved ---> updating the price and status");
        ordersService.updateOrders2(message).subscribe();
    }

    @KafkaListener(topics = "order-update-fail", groupId = "didiKerenGroup")
    public void consumeOrderResponseFail(OrdersDTOSend message) {
        log.info("Product not approved ---> updating the status to FAIL");
        ordersService.updateOrdersFail(message).subscribe();
    }


}
