package com.project.order.orders;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.order.exception.OrderException;
import com.project.order.orderItem.OrderItem;
import com.project.order.orderItem.OrderItemRepository;
import com.project.order.kafka.OrderProducer;
import com.example.DTO.order.OrdersDTO;
import com.example.DTO.order.OrdersDTOSend;
import com.example.enume.OrderStatusEnum;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrdersService {

        @Autowired
        private OrdersRepository ordersRepository;

        @Autowired
        private OrderItemRepository orderItemRepository;

        @Autowired
        private OrderProducer orderProducer;

        public Flux<Orders> getAllOrders() {
                return ordersRepository.findAll()
                                .switchIfEmpty(Mono.error(
                                                new OrderException(
                                                                String.format("Can't display all orders"))));
        }

        public Mono<Orders> getOrdersById(Long id) {
                return ordersRepository.findById(id)
                                .switchIfEmpty(Mono.error(
                                                new OrderException(
                                                                String.format("Order not found. Id: %d", id))));
        }

        // untuk menambahkan order
        public Mono<Object> addOrders(@Valid OrdersDTO ordersDTO) {
                Orders order = Orders.builder()
                                .billing_address(ordersDTO.getBilling_address())
                                .customer_id(ordersDTO.getCustomer_id())
                                .order_status(OrderStatusEnum.PROCESSING.name())
                                .payment_method(ordersDTO.getPayment_method())
                                .shipping_address(ordersDTO.getShipping_address())
                                .total_amount(ordersDTO.getTotal_amount())
                                .build();
                return ordersRepository.save(order)
                                .flatMap(savedOrder -> {
                                        OrderItem orderItems = OrderItem.builder()
                                                        .price(ordersDTO.getOrderItems().getPrice())
                                                        .product_id(ordersDTO.getOrderItems().getProduct_id())
                                                        .quantity(ordersDTO.getOrderItems().getQuantity())
                                                        .order_id(savedOrder.getId())
                                                        .build();

                                        return orderItemRepository.save(orderItems)
                                                        .then(ordersRepository.findById(savedOrder.getId()))
                                                        .flatMap(updatedOrder -> {
                                                                OrdersDTOSend ordersDTOSend = new OrdersDTOSend(
                                                                                updatedOrder.getId(), ordersDTO,
                                                                                updatedOrder.getOrder_date());
                                                                orderProducer.sendOrderRequest(ordersDTOSend);
                                                                return Mono.just(ResponseEntity.ok(updatedOrder));
                                                        });

                                });
        }

        // untuk mengupdate price dari order yang sudah dibuat
        public Mono<Orders> updateOrders2(OrdersDTOSend ordersDTO) {
                Long id = ordersDTO.getId();
                Float price = ordersDTO.getOrdersDTO().getOrderItems().getPrice();
                Float total_amount = ordersDTO.getOrdersDTO().getOrderItems().getPrice()
                                * ordersDTO.getOrdersDTO().getOrderItems().getQuantity();
                log.info(price.toString() + " I want to change the price to id ");
                log.info("I want to change the total amount to: " + total_amount.toString());
                log.info("id:" + id.toString());

                return ordersRepository.findById(id)
                                .flatMap(savedOrder -> {
                                        savedOrder.setOrder_status(OrderStatusEnum.COMPLETED.name());
                                        savedOrder.setTotal_amount(total_amount);
                                        return ordersRepository.save(savedOrder)
                                                        .flatMap(updateOrder -> {
                                                                Long orderItemId = updateOrder.getId();
                                                                return orderItemRepository.findById(orderItemId)
                                                                                .flatMap(orderItem -> {
                                                                                        orderItem.setPrice(price);
                                                                                        log.info("Price has been updated to: "
                                                                                                        + price.toString());
                                                                                        return orderItemRepository
                                                                                                        .save(orderItem)
                                                                                                        .then(Mono.just(updateOrder));
                                                                                });
                                                        });
                                }).switchIfEmpty(Mono.error(
                                                new OrderException(
                                                                String.format("Order not found. Id: %d", id))));
        }

        // untuk mengupdate status dari order yang sudah dibuat menjadi FAIL
        public Mono<Orders> updateOrdersFail(OrdersDTOSend ordersDTO) {
                Long id = ordersDTO.getId();
                Float price = ordersDTO.getOrdersDTO().getOrderItems().getPrice();
                Float total_amount = ordersDTO.getOrdersDTO().getOrderItems().getPrice()
                                * ordersDTO.getOrdersDTO().getOrderItems().getQuantity();
                log.info(price.toString() + " I want to change the price to id ");
                log.info("I want to change the total amount to: " + total_amount.toString());
                log.info("id:" + id.toString());

                return ordersRepository.findById(id)
                                .flatMap(savedOrder -> {
                                        savedOrder.setOrder_status(OrderStatusEnum.FAILED.name());
                                        savedOrder.setTotal_amount(total_amount);
                                        return ordersRepository.save(savedOrder)
                                                        .flatMap(updateOrder -> {
                                                                Long orderItemId = updateOrder.getId();
                                                                return orderItemRepository.findById(orderItemId)
                                                                                .flatMap(orderItem -> {
                                                                                        orderItem.setPrice(price);
                                                                                        log.info("Price has been updated to: "
                                                                                                        + price.toString());
                                                                                        return orderItemRepository
                                                                                                        .save(orderItem)
                                                                                                        .then(Mono.just(updateOrder));
                                                                                });
                                                        });
                                }).switchIfEmpty(Mono.error(
                                                new OrderException(
                                                                String.format("Order not found. Id: %d", id))));
        }

        public Mono<Orders> updateOrders(Long id, Orders orders) {
                return ordersRepository.findById(id)
                                .map(Optional::of)
                                .defaultIfEmpty(Optional.empty())
                                .flatMap(optionalOrders -> {
                                        if (optionalOrders.isPresent()) {
                                                orders.setId(id);
                                                return ordersRepository.save(orders);
                                        }
                                        return Mono.empty();
                                })
                                .switchIfEmpty(Mono.error(
                                                new OrderException(
                                                                String.format("Cannot update order"))));
        }

        public Mono<Void> deleteOrdersById(Long id) {
                return ordersRepository.deleteById(id);
        }

        public Mono<Void> deleteAllOrders() {
                return ordersRepository.deleteAll();
        }

}

// return orderItemRepository.findById(id)
// .doOnNext(order -> log.info("Product found: " + order.toString()))
// .flatMap(savedOrder -> {
// savedOrder.setPrice(price);
// log.info(price.toString() + "I want to change the price");
// log.info("Saved Order will be: " + savedOrder.toString());
// return orderItemRepository.save(savedOrder)
// .then(ordersRepository.findById(savedOrder.getOrder_id()));
// })
// .doOnError(error -> log.error("Error occurred: " + error.getMessage()));

// return ordersRepository.save(order)
// .flatMap(savedOrder -> {
// List<OrderItem> orderItems = ordersDTO.getOrderItems().stream()
// .map(itemDTO -> OrderItem.builder()
// .price(itemDTO.getPrice())
// .product_id(itemDTO.getProduct_id())
// .quantity(itemDTO.getQuantity())
// .order_id(savedOrder.getId())
// .build())
// .collect(Collectors.toList());

// orderProducer.sendOrderRequest(orderItems);
// orderItems.forEach(orderItem -> {
// // String message = String.format("Check product %d for quantity %d",
// // orderItem.getProduct_id(), orderItem.getQuantity());
// // orderProducer.sendOrderRequest(orderItem);
// });
// return orderItemRepository.saveAll(orderItems)
// .collectList()
// .then(ordersRepository.findById(savedOrder.getId()))
// .map(updatedOrder -> ResponseEntity.ok(updatedOrder));
// });

// return ordersRepository.save(order)
// .flatMap(savedOrder -> {
// List<OrderItem> orderItems = ordersDTO.getOrderItems().stream()
// .map(itemDTO -> OrderItem.builder()
// .price(itemDTO.getPrice())
// .product_id(itemDTO.getProduct_id())
// .quantity(itemDTO.getQuantity())
// .order_id(savedOrder.getId())
// .build())
// .collect(Collectors.toList());

// return orderItemRepository.saveAll(orderItems)
// .collectList()
// .then(ordersRepository.findById(savedOrder.getId()))
// .map(updatedOrder -> ResponseEntity.ok(updatedOrder));
// });

// return ordersRepository.save(order)
// .flatMap(savedOrder -> {
// try {
// // Serialize OrdersDTO to JSON string
// String message = objectMapper.writeValueAsString(ordersDTO);
// // Send message to Kafka
// orderProducer.sendOrderRequest(message);
// } catch (JsonProcessingException e) {
// return Mono.error(
// new RuntimeException("Error serializing order DTO", e));
// }
// return Mono.just(savedOrder);
// });