package com.project.payment.payment;

import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.order.OrdersDTOSend;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // display all transactions
    @GetMapping
    public Flux<Payment> getAllPayment() {
        return paymentService.getAllPayment();
    }

    // display transaction by id
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Payment>> getById(@PathVariable Long id) {
        return paymentService.getById(id)
                .map(payment1 -> ResponseEntity.ok(payment1))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    // add transaction
    @PostMapping
    public Mono<ResponseEntity<Payment>> addPayment(@Valid @RequestBody Payment payment) {
        return paymentService.addPayment(payment)
                .map(payment1 -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(payment1))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // add transaction_details
    @PostMapping("/create/transaction")
    public Mono<ResponseEntity<Payment>> addTransactionDetails(@Valid @RequestBody OrdersDTOSend ordersDto) {
        return paymentService.addTransactionDetails(ordersDto)
                .map(payment1 -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(payment1))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create/transaction-fail")
    public Mono<ResponseEntity<Payment>> addTransactionDetailsFail(@Valid @RequestBody OrdersDTOSend ordersDto) {
        return paymentService.addTransactionDetailsFail(ordersDto)
                .map(payment1 -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(payment1))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // update the transaction
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Payment>> updatePayment(@PathVariable Long id, @Valid @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment)
                .map(payment1 -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(payment1));
    }

    // delete the transaction
    @DeleteMapping
    public Mono<Void> deleteAll() {
        return paymentService.deleteAll();
    }

    // delete the transaction by ID
    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable Long id) {
        return paymentService.deleteById(id);
    }

}
