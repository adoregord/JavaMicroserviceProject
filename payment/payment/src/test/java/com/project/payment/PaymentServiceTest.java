package com.project.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DTO.order.OrderItemDTO;
import com.example.DTO.order.OrdersDTO;
import com.example.DTO.order.OrdersDTOSend;
import com.project.payment.exception.PaymentException;
import com.project.payment.payment.Payment;
import com.project.payment.payment.PaymentRepository;
import com.project.payment.payment.PaymentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                .id(1L)
                .amount(100.0F)
                .order_id(1L)
                .payment_date(LocalDateTime.now())
                .mode("CREDIT_CARD")
                .status("APPROVED")
                .reference_number("12345")
                .build();
    }

    @Test
    void getAllPayment_Success() {
        when(paymentRepository.findAll()).thenReturn(Flux.just(payment));

        StepVerifier.create(paymentService.getAllPayment())
                .expectNext(payment)
                .verifyComplete();
    }

    @Test
    void getAllPayment_Error() {
        when(paymentRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(paymentService.getAllPayment())
                .expectError(PaymentException.class)
                .verify();
    }

    @Test
    void getById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Mono.just(payment));

        StepVerifier.create(paymentService.getById(1L))
                .expectNext(payment)
                .verifyComplete();
    }

    @Test
    void getById_Error() {
        when(paymentRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(paymentService.getById(1L))
                .expectError(PaymentException.class)
                .verify();
    }

    @Test
    void addPayment_Success() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(payment));

        StepVerifier.create(paymentService.addPayment(payment))
                .expectNext(payment)
                .verifyComplete();
    }

    @Test
    void addPayment_Error() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(paymentService.addPayment(payment))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void updatePayment_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Mono.just(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(payment));

        StepVerifier.create(paymentService.updatePayment(1L, payment))
                .expectNext(payment)
                .verifyComplete();
    }

    @Test
    void updatePayment_Error() {
        when(paymentRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(paymentService.updatePayment(1L, payment))
                .expectError(PaymentException.class)
                .verify();
    }

    @Test
    void deleteAll_Success() {
        when(paymentRepository.deleteAll()).thenReturn(Mono.empty());

        StepVerifier.create(paymentService.deleteAll())
                .verifyComplete();
    }

    @Test
    void deleteById_Success() {
        when(paymentRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(paymentService.deleteById(1L))
                .verifyComplete();
    }

    @Test
    void addTransactionDetails_Success() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        OrdersDTO ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 100.0F,
                orderItemDTO);
        
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(payment));

        StepVerifier.create(paymentService.addTransactionDetails(ordersDTOSend))
                .expectNext(payment)
                .verifyComplete();
    }

    @Test
    void addTransactionDetails_Error() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        OrdersDTO ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 100.0F,
                orderItemDTO);
        
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(paymentService.addTransactionDetails(ordersDTOSend))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void addTransactionDetailsFail_Success() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        OrdersDTO ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 100.0F,
                orderItemDTO);
        
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(payment));

        StepVerifier.create(paymentService.addTransactionDetailsFail(ordersDTOSend))
                .expectNext(payment)
                .verifyComplete();
    }

    @Test
    void addTransactionDetailsFail_Error() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        OrdersDTO ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 100.0F,
                orderItemDTO);
        
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(paymentService.addTransactionDetailsFail(ordersDTOSend))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void addTransactionDetailsSuccess_Success() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        OrdersDTO ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 100.0F,
                orderItemDTO);
        
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(payment));

        StepVerifier.create(paymentService.addTransactionDetailsSuccess(ordersDTOSend))
                .expectNext(payment)
                .verifyComplete();
    }

    @Test
    void addTransactionDetailsSuccess_Error() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        OrdersDTO ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 100.0F,
                orderItemDTO);
        
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(paymentService.addTransactionDetailsSuccess(ordersDTOSend))
                .expectError(RuntimeException.class)
                .verify();
    }
}
