package com.project.payment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.DTO.balance.BalanceDTO;
import com.example.DTO.order.OrdersDTOSend;
import com.project.payment.balance.Balance;
import com.project.payment.balance.BalanceController;
import com.project.payment.balance.BalanceService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@WebFluxTest(controllers = BalanceController.class)
@ExtendWith(SpringExtension.class)
public class BalanceControllerTest {

    @MockBean
    private BalanceService balanceService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetAllBalance_OK() {
        Balance balance = new Balance(1L, 100.0F, 1L);

        when(balanceService.getBalance()).thenReturn(Flux.just(balance));

        webTestClient.get().uri("/balance")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Balance.class)
                .hasSize(1)
                .contains(balance);
    }

    @Test
    void testGetBalanceById_OK() {
        Balance balance = new Balance(1L, 100.0F, 1L);

        when(balanceService.getBalanceById(anyLong())).thenReturn(Mono.just(balance));

        webTestClient.get().uri("/balance/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Balance.class)
                .isEqualTo(balance);
    }

    @Test
    void testGetBalanceById_NotFound() {
        when(balanceService.getBalanceById(anyLong())).thenReturn(Mono.empty());

        webTestClient.get().uri("/balance/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testAddBalanceInfo_OK() {
        BalanceDTO balanceDTO = new BalanceDTO(100.0F);
        Balance balance = new Balance(1L, 100.0F, 1L);

        when(balanceService.addBalanceInfo(any(BalanceDTO.class))).thenReturn(Mono.just(balance));

        webTestClient.post().uri("/balance/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(balanceDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Balance.class)
                .isEqualTo(balance);
    }

    @Test
    void testAddBalanceInfo_NotFound() {
        BalanceDTO balanceDTO = new BalanceDTO(100.0F);

        when(balanceService.addBalanceInfo(any(BalanceDTO.class))).thenReturn(Mono.empty());

        webTestClient.post().uri("/balance/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(balanceDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCheckBalanceAndDeduct_OK() {
        OrdersDTOSend ordersDTO = new OrdersDTOSend();
        when(balanceService.checkBalanceAndDeduct(any(OrdersDTOSend.class))).thenReturn(Mono.just(ordersDTO));

        webTestClient.post().uri("/balance/check-balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OrdersDTOSend.class)
                .isEqualTo(ordersDTO);
    }

    @Test
    void testCheckBalanceAndDeduct_NotFound() {
        OrdersDTOSend ordersDTO = new OrdersDTOSend();

        when(balanceService.checkBalanceAndDeduct(any(OrdersDTOSend.class))).thenReturn(Mono.empty());

        webTestClient.post().uri("/balance/check-balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateBalance_OK() {
        BalanceDTO balanceDTO = new BalanceDTO(100.0F);
        Balance balance = new Balance(1L, 200.0F, 1L);

        when(balanceService.updateBalance(anyLong(), any(BalanceDTO.class))).thenReturn(Mono.just(balance));

        webTestClient.patch().uri("/balance/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(balanceDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Balance.class)
                .isEqualTo(balance);
    }

    @Test
    void testUpdateBalance_NotFound() {
        BalanceDTO balanceDTO = new BalanceDTO(100.0F);

        when(balanceService.updateBalance(anyLong(), any(BalanceDTO.class))).thenReturn(Mono.empty());

        webTestClient.patch().uri("/balance/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(balanceDTO)
                .exchange()
                .expectStatus().isNotFound();
    }
}
