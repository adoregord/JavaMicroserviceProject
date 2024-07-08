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
import com.example.DTO.balance.BalanceDTO;
import com.example.DTO.order.OrderItemDTO;
import com.example.DTO.order.OrdersDTO;
import com.example.DTO.order.OrdersDTOSend;
import com.project.payment.balance.Balance;
import com.project.payment.balance.BalanceRepository;
import com.project.payment.balance.BalanceService;
import com.project.payment.exception.PaymentException;
import com.project.payment.payment.Payment;
import com.project.payment.payment.PaymentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private BalanceService balanceService;

    private Balance balance;

    @BeforeEach
    void setUp() {
        balance = Balance.builder()
                .id(1L)
                .amount(100.0F)
                .customer_id(1L)
                .build();

    }

    @Test
    void getBalance_Success() {
        when(balanceRepository.findAll()).thenReturn(Flux.just(balance));

        StepVerifier.create(balanceService.getBalance())
                .expectNext(balance)
                .verifyComplete();
    }

    @Test
    void getBalance_Error() {
        when(balanceRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(balanceService.getBalance())
                .expectError(PaymentException.class)
                .verify();
    }

    @Test
    void getBalanceById_Success() {
        when(balanceRepository.findById(1L)).thenReturn(Mono.just(balance));

        StepVerifier.create(balanceService.getBalanceById(1L))
                .expectNext(balance)
                .verifyComplete();
    }

    @Test
    void getBalanceById_Error() {
        when(balanceRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(balanceService.getBalanceById(1L))
                .expectError(PaymentException.class)
                .verify();
    }

    @Test
    void addBalanceInfo_Success() {
        BalanceDTO balanceDTO = new BalanceDTO(100.0F);
        when(balanceRepository.save(any(Balance.class))).thenReturn(Mono.just(balance));
        when(balanceRepository.findById(any(Long.class))).thenReturn(Mono.just(balance));

        StepVerifier.create(balanceService.addBalanceInfo(balanceDTO))
                .expectNextMatches(savedBalance -> savedBalance.getAmount().equals(100.0F))
                .verifyComplete();
    }

    @Test
    void addBalanceInfo_Error() {
        BalanceDTO balanceDTO = new BalanceDTO(100.0F);
        when(balanceRepository.save(any(Balance.class))).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier.create(balanceService.addBalanceInfo(balanceDTO))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void updateBalance_Success() {
        BalanceDTO balanceDTO = new BalanceDTO(50.0F);
        when(balanceRepository.findById(1L)).thenReturn(Mono.just(balance));
        when(balanceRepository.save(any(Balance.class))).thenReturn(Mono.just(balance));

        StepVerifier.create(balanceService.updateBalance(1L, balanceDTO))
                .expectNextMatches(updatedBalance -> updatedBalance.getAmount().equals(150.0F))
                .verifyComplete();
    }

    @Test
    void checkBalanceAndDeduct_Success() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        OrdersDTO ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 100.0F,
                orderItemDTO);
        
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, LocalDateTime.now());

        when(balanceRepository.findById(1L)).thenReturn(Mono.just(balance));
        when(balanceRepository.save(any(Balance.class))).thenReturn(Mono.just(balance));
        when(paymentService.addTransactionDetailsSuccess(any(OrdersDTOSend.class)))
                .thenReturn(Mono.just(new Payment()));

        StepVerifier.create(balanceService.checkBalanceAndDeduct(ordersDTOSend))
                .expectNextMatches(order -> order.getOrdersDTO().getOrder_status().equals("COMPLETED"))
                .verifyComplete();
    }

    @Test
    void checkBalanceAndDeduct_Error() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(150.0F, 1L, 1);
        OrdersDTO ordersDTO = new OrdersDTO("123 Main St", 1L, "aa", "Credit Card", "123 Main St", 150.0F,
                orderItemDTO);
        
        
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend(1L, ordersDTO, LocalDateTime.now());

        when(balanceRepository.findById(1L)).thenReturn(Mono.just(balance));
        when(paymentService.addTransactionDetailsFail(any(OrdersDTOSend.class))).thenReturn(Mono.just(new Payment()));

        StepVerifier.create(balanceService.checkBalanceAndDeduct(ordersDTOSend))
                .expectNextMatches(order -> order.getOrdersDTO().getOrder_status().equals("FAILED"))
                .verifyComplete();
    }
}