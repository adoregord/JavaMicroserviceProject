package com.project.payment.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.balance.BalanceDTO;
import com.example.DTO.order.OrdersDTOSend;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    // diaplay all balance
    @GetMapping // display all balance
    public Flux<Balance> getAllBalance() {
        return balanceService.getBalance();
    }

    // display balance by id
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Balance>> getBalanceById(@PathVariable Long id) {
        return balanceService.getBalanceById(id).map(balance -> ResponseEntity
                .status(HttpStatus.OK)
                .body(balance))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // add balance info
    @PostMapping("/add")
    public Mono<ResponseEntity<Balance>> addBalanceInfo(@Valid @RequestBody BalanceDTO balanceDTO) {
        return balanceService.addBalanceInfo(balanceDTO)
                .map(balance -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(balance))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // checking and deduct customer's balance
    @PostMapping("/check-balance")
    public Mono<ResponseEntity<OrdersDTOSend>> checkBalanceAndDeduct(@Valid @RequestBody OrdersDTOSend ordersDTO) {
        return balanceService.checkBalanceAndDeduct(ordersDTO)
                .map(balance -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(balance))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // update balance by id
    @PatchMapping("/update/{id}")
    public Mono<ResponseEntity<Balance>> updateBalance(@PathVariable Long id,
            @Valid @RequestBody BalanceDTO balanceDTO) {
        return balanceService.updateBalance(id, balanceDTO)
                .map(balance -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(balance))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
