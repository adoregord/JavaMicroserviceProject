package com.project.payment.payment;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DTO.order.OrdersDTOSend;
import com.example.enume.PaymentStatusEnum;
import com.project.payment.exception.PaymentException;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Flux<Payment> getAllPayment() {
        return paymentRepository.findAll()
                .switchIfEmpty(Mono.error(
                        new PaymentException(
                                String.format("Can't display all product"))));

    }

    public Mono<Payment> getById(Long id) {
        return paymentRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new PaymentException(
                                String.format("Can't display payment Id:%d", id))));
    }

    public Mono<Payment> addPayment(Payment payment) {
        return paymentRepository.save(payment)
                .switchIfEmpty(Mono.error(
                        new PaymentException(
                                String.format("Can't add payment"))));
    }

    public Mono<Payment> updatePayment(Long id, Payment payment) {
        return paymentRepository.findById(Long.valueOf(id))
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalBook -> {
                    if (optionalBook.isPresent()) {
                        payment.setId(id);
                        return paymentRepository.save(payment);
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(
                        new PaymentException(
                                String.format("Can't update payment"))));
    }

    public Mono<Void> deleteAll() {
        return paymentRepository.deleteAll();
    }

    public Mono<Void> deleteById(Long id) {
        return paymentRepository.deleteById(id);
    }

    public Mono<Payment> addTransactionDetails(@Valid OrdersDTOSend ordersDTO) {
        Long customerId = ordersDTO.getOrdersDTO().getCustomer_id();
        String referenceNumber = generateReferenceNumber(customerId);

        Payment payment = Payment.builder()
                .amount(ordersDTO.getOrdersDTO().getTotal_amount())
                .order_id(ordersDTO.getId())
                .payment_date(ordersDTO.getOrder_date())
                .mode(ordersDTO.getOrdersDTO().getPayment_method())
                .status(PaymentStatusEnum.APPROVED.name())
                .reference_number(referenceNumber)
                .build();

        return paymentRepository.save(payment)
                .switchIfEmpty(Mono.error(
                        new PaymentException(
                                String.format("Can't add payment details"))));
    }

    public Mono<Payment> addTransactionDetailsFail(@Valid OrdersDTOSend ordersDTO) {
        Long customerId = ordersDTO.getOrdersDTO().getCustomer_id();
        String referenceNumber = generateReferenceNumber(customerId);

        Payment payment = Payment.builder()
                .amount(ordersDTO.getOrdersDTO().getTotal_amount())
                .order_id(ordersDTO.getId())
                .payment_date(ordersDTO.getOrder_date())
                .mode(ordersDTO.getOrdersDTO().getPayment_method())
                .status(PaymentStatusEnum.REJECTED.name())
                .reference_number(referenceNumber)
                .build();

        return paymentRepository.save(payment)
                .switchIfEmpty(Mono.error(
                        new PaymentException(
                                String.format("Can't add payment details"))));
    }

    public Mono<Payment> addTransactionDetailsSuccess(@Valid OrdersDTOSend ordersDTO) {
        Long customerId = ordersDTO.getOrdersDTO().getCustomer_id();
        String referenceNumber = generateReferenceNumber(customerId);

        Payment payment = Payment.builder()
                .amount(ordersDTO.getOrdersDTO().getTotal_amount())
                .order_id(ordersDTO.getId())
                .payment_date(ordersDTO.getOrder_date())
                .mode(ordersDTO.getOrdersDTO().getPayment_method())
                .status(PaymentStatusEnum.APPROVED.name())
                .reference_number(referenceNumber)
                .build();

        return paymentRepository.save(payment)
                .switchIfEmpty(Mono.error(
                        new PaymentException(
                                String.format("Can't add payment details"))));
    }
    

    private String generateReferenceNumber(Long customerId) {
        // Length of the random part of the reference number
        int length = 10;
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        long timestamp = Instant.now().toEpochMilli();

        SecureRandom random = new SecureRandom();
        String randomChars = IntStream.range(0, length)
                .map(i -> random.nextInt(alphanumeric.length()))
                .mapToObj(randomIndex -> String.valueOf(alphanumeric.charAt(randomIndex)))
                .collect(Collectors.joining());
        return customerId + "-" + timestamp + randomChars;
    }

}
