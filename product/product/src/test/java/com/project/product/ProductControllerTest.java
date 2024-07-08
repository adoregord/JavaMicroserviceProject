package com.project.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.DTO.order.OrdersDTOSend;
import com.example.DTO.product.ProductDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .price(29.99f)
                .category("Category1")
                .created_at(LocalDateTime.now())
                .description("Description1")
                .image_url("ImageUrl1")
                .stock_quantity(10)
                .published_at(LocalDateTime.now())
                .build();

        productDTO = new ProductDTO(
                29.99f,
                "Category1",
                "Description1",
                "ImageUrl1",
                10
        );
    }

    @Test
    void getAllProductOk() {
        when(productService.getAllProduct()).thenReturn(Flux.just(product));

        webTestClient.get().uri("/inventory")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(1)
                .contains(product);
    }

    @Test
    void addProductOk() {
        when(productService.addProduct(any(ProductDTO.class))).thenReturn(Mono.just(product));

        webTestClient.post().uri("/inventory/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class)
                .isEqualTo(product);
    }

    @Test
    void getByIdOk() {
        when(productService.getById(anyLong())).thenReturn(Mono.just(product));

        webTestClient.get().uri("/inventory/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(product);
    }

    @Test
    void getByIdNotFound() {
        when(productService.getById(anyLong())).thenReturn(Mono.empty());

        webTestClient.get().uri("/inventory/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateProductOk() {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(Mono.just(product));

        webTestClient.put().uri("/inventory/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class)
                .isEqualTo(product);
    }

    @Test
    void addStockQuantityOk() {
        when(productService.addStockQuantity(anyLong(), any(Integer.class))).thenReturn(Mono.just(product));

        webTestClient.patch().uri("/inventory/add/{id}?sumItem={sumItem}", 1L, 5)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(product);
    }

    @Test
    void addStockQuantityNotFound() {
        when(productService.addStockQuantity(anyLong(), any(Integer.class))).thenReturn(Mono.empty());

        webTestClient.patch().uri("/inventory/add/{id}?sumItem={sumItem}", 1L, 5)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void checkAndDeductStockOk() {
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend();
        when(productService.checkAndDeductStock(any(OrdersDTOSend.class))).thenReturn(Mono.just(ordersDTOSend));

        webTestClient.post().uri("/inventory/check-deduct-stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersDTOSend)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrdersDTOSend.class)
                .isEqualTo(ordersDTOSend);
    }

    @Test
    void reAddStockOk() {
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend();
        when(productService.reAddStock(any(OrdersDTOSend.class))).thenReturn(Mono.just(ordersDTOSend));

        webTestClient.post().uri("/inventory/re-add-stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersDTOSend)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrdersDTOSend.class)
                .isEqualTo(ordersDTOSend);
    }

    @Test
    void deleteByIdOk() {
        when(productService.deleteById(anyLong())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/inventory/{id}", 1L)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteAllOk() {
        when(productService.deleteAll()).thenReturn(Mono.empty());

        webTestClient.delete().uri("/inventory")
                .exchange()
                .expectStatus().isOk();
    }
}
