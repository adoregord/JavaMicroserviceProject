package com.project.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.DTO.order.OrderItemDTO;
import com.example.DTO.order.OrdersDTO;
import com.project.order.exception.OrderException;
import com.project.order.orders.Orders;
import com.project.order.orders.OrdersController;
import com.project.order.orders.OrdersService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = OrdersController.class)
public class OrdersControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrdersService ordersService;

    private final String url = "/orders";

    @Test
    void getAllOrdersOk() {
        var order1 = Orders.builder()
                .id(1L)
                .billing_address("Billing Address 1")
                .customer_id(1L)
                .order_status("PROCESSING")
                .payment_method("Credit Card")
                .shipping_address("Shipping Address 1")
                .total_amount(100.0f)
                .order_date(LocalDateTime.now())
                .build();

        var order2 = Orders.builder()
                .id(2L)
                .billing_address("Billing Address 2")
                .customer_id(2L)
                .order_status("COMPLETED")
                .payment_method("PayPal")
                .shipping_address("Shipping Address 2")
                .total_amount(200.0f)
                .order_date(LocalDateTime.now())
                .build();

        when(ordersService.getAllOrders()).thenReturn(Flux.just(order1, order2));

        webTestClient
                .get()
                .uri(url + "/all")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Orders.class)
                .consumeWith(result -> {
                    var orders = result.getResponseBody();
                    assertNotNull(orders);
                    assertEquals(2, orders.size());
                    assertEquals(order1.getId(), orders.get(0).getId());
                    assertEquals(order2.getId(), orders.get(1).getId());
                });
    }

    @Test
    void getOrdersByIdOk() {
        var order = Orders.builder()
                .id(1L)
                .billing_address("Billing Address")
                .customer_id(1L)
                .order_status("PROCESSING")
                .payment_method("Credit Card")
                .shipping_address("Shipping Address")
                .total_amount(100.0f)
                .order_date(LocalDateTime.now())
                .build();

        when(ordersService.getOrdersById(order.getId())).thenReturn(Mono.just(order));

        webTestClient
                .get()
                .uri(url + "/" + order.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Orders.class)
                .consumeWith(result -> {
                    var existOrder = result.getResponseBody();
                    assertNotNull(existOrder);
                    assertEquals(order.getId(), existOrder.getId());
                    assertEquals(order.getBilling_address(), existOrder.getBilling_address());
                });
    }

    @Test
    void getOrdersByIdErrorNotFound() {
        var orderId = 1L;
        when(ordersService.getOrdersById(orderId)).thenReturn(Mono.empty());

        webTestClient
                .get()
                .uri(url + "/" + orderId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void addOrdersOk() {
        var orderDTO = new OrdersDTO(
                "Billing Address",
                1L,
                "PROCESSING",
                "Credit Card",
                "Shipping Address",
                100.0f,
                new OrderItemDTO(50.0f, 1L, 2)
        );

        var order = Orders.builder()
                .id(1L)
                .billing_address(orderDTO.getBilling_address())
                .customer_id(orderDTO.getCustomer_id())
                .order_status(orderDTO.getOrder_status())
                .payment_method(orderDTO.getPayment_method())
                .shipping_address(orderDTO.getShipping_address())
                .total_amount(orderDTO.getTotal_amount())
                .build();

        when(ordersService.addOrders(any(OrdersDTO.class))).thenReturn(Mono.just(order));

        webTestClient
                .post()
                .uri(url + "/create")
                .bodyValue(orderDTO)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Orders.class)
                .consumeWith(result -> {
                    var createdOrder = result.getResponseBody();
                    assertNotNull(createdOrder);
                    assertEquals(order.getBilling_address(), createdOrder.getBilling_address());
                });
    }

    @Test
    void addOrdersError() {
        var orderDTO = new OrdersDTO(
                "Billing Address",
                1L,
                "PROCESSING",
                "Credit Card",
                "Shipping Address",
                100.0f,
                new OrderItemDTO(50.0f, 1L, 2)
        );

        when(ordersService.addOrders(any(OrdersDTO.class))).thenReturn(Mono.empty());

        webTestClient
                .post()
                .uri(url + "/create")
                .bodyValue(orderDTO)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void updateOrdersOk() {
        var order = Orders.builder()
                .id(1L)
                .billing_address("Billing Address")
                .customer_id(1L)
                .order_status("PROCESSING")
                .payment_method("Credit Card")
                .shipping_address("Shipping Address")
                .total_amount(100.0f)
                .build();

        when(ordersService.updateOrders(eq(order.getId()), any(Orders.class))).thenReturn(Mono.just(order));

        webTestClient
                .put()
                .uri(url + "/update/" + order.getId())
                .bodyValue(order)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Orders.class)
                .consumeWith(result -> {
                    var updatedOrder = result.getResponseBody();
                    assertNotNull(updatedOrder);
                    assertEquals(order.getId(), updatedOrder.getId());
                    assertEquals(order.getBilling_address(), updatedOrder.getBilling_address());
                });
    }

    @Test
    void updateOrdersError() {
        var order = Orders.builder()
                .id(1L)
                .billing_address("Billing Address")
                .customer_id(1L)
                .order_status("")
                .payment_method("Credit Card")
                .shipping_address("Shipping Address")
                .total_amount(100.00f)
                .build();

        when(ordersService.updateOrders(eq(order.getId()), any(Orders.class))).thenReturn(Mono.empty());

        webTestClient
                .put()
                .uri(url + "/update/" + order.getId())
                .bodyValue(order)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteOrdersByIdOk() {
        var orderId = 1L;
        when(ordersService.deleteOrdersById(orderId)).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(url + "/" + orderId)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void deleteOrdersByIdError() {
        var orderId = 1L;
        when(ordersService.deleteOrdersById(orderId)).thenReturn(Mono.error(new OrderException("Order not found")));

        webTestClient
                .delete()
                .uri(url + "/" + orderId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteAllOrdersOk() {
        when(ordersService.deleteAllOrders()).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(url)
                .exchange()
                .expectStatus()
                .isOk();
    }
}


