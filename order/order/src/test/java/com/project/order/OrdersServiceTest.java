package com.project.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.example.DTO.order.OrderItemDTO;
import com.example.DTO.order.OrdersDTO;
import com.example.DTO.order.OrdersDTOSend;
import com.example.enume.OrderStatusEnum;
import com.project.order.exception.OrderException;
import com.project.order.orderItem.OrderItem;
import com.project.order.orderItem.OrderItemRepository;
import com.project.order.kafka.OrderProducer;
import com.project.order.orders.Orders;
import com.project.order.orders.OrdersRepository;
import com.project.order.orders.OrdersService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrdersService ordersService;

    private OrdersDTO ordersDTO;
    private Orders order;
    private OrderItem orderItem;
    private OrdersDTOSend ordersDTOSend;
    private OrderItemDTO orderItemDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 100.0F, orderItemDTO);

        order = Orders.builder()
                .id(1L)
                .billing_address("123 Main St")
                .customer_id(1L)
                .order_status(OrderStatusEnum.PROCESSING.name())
                .payment_method("Credit Card")
                .shipping_address("123 Main St")
                .total_amount(100.0F)
                .build();

        orderItem = OrderItem.builder()
                .id(1L)
                .order_id(1L)
                .price(100.0F)
                .product_id(1L)
                .quantity(1)
                .build();

        ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, order.getOrder_date());
    }

    @Test
    public void testGetAllOrders_Success() {
        when(ordersRepository.findAll()).thenReturn(Flux.just(order));

        StepVerifier.create(ordersService.getAllOrders())
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    public void testGetAllOrders_Error() {
        when(ordersRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(ordersService.getAllOrders())
                .expectError(OrderException.class)
                .verify();
    }

    @Test
    public void testGetOrdersById_Success() {
        when(ordersRepository.findById(1L)).thenReturn(Mono.just(order));

        StepVerifier.create(ordersService.getOrdersById(1L))
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    public void testGetOrdersById_Error() {
        when(ordersRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(ordersService.getOrdersById(1L))
                .expectError(OrderException.class)
                .verify();
    }

    @Test
    public void testAddOrders_Success() {
        when(ordersRepository.save(any(Orders.class))).thenReturn(Mono.just(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(Mono.just(orderItem));
        when(ordersRepository.findById(1L)).thenReturn(Mono.just(order));

        StepVerifier.create(ordersService.addOrders(ordersDTO))
                .expectNextMatches(response -> {
                    ResponseEntity<?> entity = (ResponseEntity<?>) response;
                    Orders savedOrder = (Orders) entity.getBody();
                    return savedOrder != null && savedOrder.getId().equals(order.getId());
                })
                .verifyComplete();
    }

    @Test
    public void testAddOrders_Error() {
        when(ordersRepository.save(any(Orders.class))).thenReturn(Mono.just(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(Mono.empty());

        StepVerifier.create(ordersService.addOrders(ordersDTO))
                .expectError()
                .verify();
    }

    @Test
    public void testUpdateOrders_Success() {
        when(ordersRepository.findById(1L)).thenReturn(Mono.just(order));
        when(ordersRepository.save(any(Orders.class))).thenReturn(Mono.just(order));

        StepVerifier.create(ordersService.updateOrders(1L, order))
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    public void testUpdateOrders_NotFound() {
        when(ordersRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(ordersService.updateOrders(1L, order))
                .expectError(OrderException.class)
                .verify();
    }

    @Test
    public void testDeleteOrdersById_Success() {
        when(ordersRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(ordersService.deleteOrdersById(1L))
                .verifyComplete();
    }

    @Test
    public void testDeleteAllOrders_Success() {
        when(ordersRepository.deleteAll()).thenReturn(Mono.empty());

        StepVerifier.create(ordersService.deleteAllOrders())
                .verifyComplete();
    }

    @Test
    public void testUpdateOrders2_Success() {
        when(ordersRepository.findById(1L)).thenReturn(Mono.just(order));
        when(ordersRepository.save(any(Orders.class))).thenReturn(Mono.just(order));
        when(orderItemRepository.findById(1L)).thenReturn(Mono.just(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(Mono.just(orderItem));

        StepVerifier.create(ordersService.updateOrders2(ordersDTOSend))
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    public void testUpdateOrders2_Error() {
        when(ordersRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(ordersService.updateOrders2(ordersDTOSend))
                .expectError()
                .verify();
    }

    @Test
    public void testUpdateOrdersFail_Error() {
        when(ordersRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(ordersService.updateOrdersFail(ordersDTOSend))
                .expectError()
                .verify();
    }

    @Test
    public void testAddOrders_ProducerSendError() {
        when(ordersRepository.save(any(Orders.class))).thenReturn(Mono.just(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(Mono.just(orderItem));

        StepVerifier.create(ordersService.addOrders(ordersDTO))
                .expectError()
                .verify();
    }

}
