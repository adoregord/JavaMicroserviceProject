package com.project.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.DTO.order.OrdersDTOSend;
import com.project.payment.payment.Payment;
import com.project.payment.payment.PaymentController;
import com.project.payment.payment.PaymentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = PaymentController.class)
@ExtendWith(SpringExtension.class)
public class PaymentControllerTest {

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetAllPayment_OK() {
        Payment payment = new Payment(1L, 100.0F, 1L, LocalDateTime.now(), "Card", "APPROVED", "REF123");

        when(paymentService.getAllPayment()).thenReturn(Flux.just(payment));

        webTestClient.get().uri("/payment")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Payment.class)
                .hasSize(1)
                .contains(payment);
    }

    @Test
    void testGetById_OK() {
        Payment payment = new Payment(1L, 100.0F, 1L, LocalDateTime.now(), "Card", "APPROVED", "REF123");

        when(paymentService.getById(anyLong())).thenReturn(Mono.just(payment));

        webTestClient.get().uri("/payment/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Payment.class)
                .isEqualTo(payment);
    }

    @Test
    void testGetById_NotFound() {
        when(paymentService.getById(anyLong())).thenReturn(Mono.empty());

        webTestClient.get().uri("/payment/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testAddPayment_OK() {
        Payment payment = new Payment(1L, 100.0F, 1L, LocalDateTime.now(), "Card", "APPROVED", "REF123");

        when(paymentService.addPayment(any(Payment.class))).thenReturn(Mono.just(payment));

        webTestClient.post().uri("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payment)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Payment.class)
                .isEqualTo(payment);
    }

    @Test
    void testAddPayment_NotFound() {
        Payment payment = new Payment(1L, 100.0F, 1L, LocalDateTime.now(), "Card", "APPROVED", "REF123");

        when(paymentService.addPayment(any(Payment.class))).thenReturn(Mono.empty());

        webTestClient.post().uri("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payment)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testAddTransactionDetails_OK() {
        OrdersDTOSend ordersDTO = new OrdersDTOSend();
        Payment payment = new Payment(1L, 100.0F, 1L, LocalDateTime.now(), "Card", "APPROVED", "REF123");

        when(paymentService.addTransactionDetails(any(OrdersDTOSend.class))).thenReturn(Mono.just(payment));

        webTestClient.post().uri("/payment/create/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Payment.class)
                .isEqualTo(payment);
    }

    @Test
    void testAddTransactionDetails_NotFound() {
        OrdersDTOSend ordersDTO = new OrdersDTOSend();

        when(paymentService.addTransactionDetails(any(OrdersDTOSend.class))).thenReturn(Mono.empty());

        webTestClient.post().uri("/payment/create/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testAddTransactionDetailsFail_OK() {
        OrdersDTOSend ordersDTO = new OrdersDTOSend();
        Payment payment = new Payment(1L, 100.0F, 1L, LocalDateTime.now(), "Card", "REJECTED", "REF123");

        when(paymentService.addTransactionDetailsFail(any(OrdersDTOSend.class))).thenReturn(Mono.just(payment));

        webTestClient.post().uri("/payment/create/transaction-fail")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Payment.class)
                .isEqualTo(payment);
    }

    @Test
    void testAddTransactionDetailsFail_NotFound() {
        OrdersDTOSend ordersDTO = new OrdersDTOSend();

        when(paymentService.addTransactionDetailsFail(any(OrdersDTOSend.class))).thenReturn(Mono.empty());

        webTestClient.post().uri("/payment/create/transaction-fail")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdatePayment_OK() {
        Payment payment = new Payment(1L, 100.0F, 1L, LocalDateTime.now(), "Card", "APPROVED", "REF123");

        when(paymentService.updatePayment(anyLong(), any(Payment.class))).thenReturn(Mono.just(payment));

        webTestClient.put().uri("/payment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payment)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Payment.class)
                .isEqualTo(payment);
    }

    @Test
    void testDeleteAll_OK() {
        when(paymentService.deleteAll()).thenReturn(Mono.empty());

        webTestClient.delete().uri("/payment")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testDeleteById_OK() {
        when(paymentService.deleteById(anyLong())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/payment/1")
                .exchange()
                .expectStatus().isOk();
    }
}

