package com.project.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.DTO.order.OrderItemDTO;
import com.example.DTO.order.OrdersDTO;
import com.example.DTO.order.OrdersDTOSend;
import com.example.DTO.product.ProductDTO;
import com.example.enume.OrderStatusEnum;
import com.project.product.exception.ProductException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;
    private OrderItemDTO orderItemDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
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
        when(productRepository.findAll()).thenReturn(Flux.just(product));

        StepVerifier.create(productService.getAllProduct())
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void getAllProductError() {
        when(productRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(productService.getAllProduct())
                .expectErrorMatches(throwable -> throwable instanceof ProductException &&
                        throwable.getMessage().equals("Can't display all product"))
                .verify();
    }

    @Test
    void addProductOk() {
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));
        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));

        StepVerifier.create(productService.addProduct(productDTO))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void addProductError() {
        when(productRepository.save(any(Product.class))).thenReturn(Mono.empty());

        StepVerifier.create(productService.addProduct(productDTO))
                .expectErrorMatches(throwable -> throwable instanceof ProductException &&
                        throwable.getMessage().equals("Cannot add product"))
                .verify();
    }

    @Test
    void getByIdOk() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));

        StepVerifier.create(productService.getById(1L))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void getByIdError() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productService.getById(1L))
                .expectErrorMatches(throwable -> throwable instanceof ProductException &&
                        throwable.getMessage().equals("Product not found. Id: 1"))
                .verify();
    }

    @Test
    void updateProductOk() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.updateProduct(1L, product))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void updateProductError() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productService.updateProduct(1L, product))
                .expectErrorMatches(throwable -> throwable instanceof ProductException &&
                        throwable.getMessage().equals("Cannot update product"))
                .verify();
    }

    @Test
    void addStockQuantityOk() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.addStockQuantity(1L, 5))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void addStockQuantityError() {
        when(productRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productService.addStockQuantity(1L, 5))
                .expectErrorMatches(throwable -> throwable instanceof ProductException &&
                        throwable.getMessage().equals("Product not found. Id: 1"))
                .verify();
    }

    @Test
    void checkAndDeductStockOk() {
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend();
        OrdersDTO ordersDTO = new OrdersDTO();
        orderItemDTO = new OrderItemDTO(100.0F, 1L, 1);
        ordersDTO.setOrderItems(orderItemDTO);
        ordersDTOSend.setOrdersDTO(ordersDTO);

        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.checkAndDeductStock(ordersDTOSend))
                .expectNext(ordersDTOSend)
                .verifyComplete();
    }

    @Test
    void checkAndDeductStockNotEnough() {
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend();
        OrdersDTO ordersDTO = new OrdersDTO();
        orderItemDTO = new OrderItemDTO(100.0F, 1L, 30);
        ordersDTO.setOrderItems(orderItemDTO);
        ordersDTO.setOrderItems(orderItemDTO); // More than available stock
        ordersDTOSend.setOrdersDTO(ordersDTO);

        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));

        StepVerifier.create(productService.checkAndDeductStock(ordersDTOSend))
                .expectNextMatches(response -> response.getOrdersDTO().getOrder_status().equals(OrderStatusEnum.FAILED.name()))
                .verifyComplete();
    }

    @Test
    void reAddStockOk() {
        OrdersDTOSend ordersDTOSend = new OrdersDTOSend();
        OrdersDTO ordersDTO = new OrdersDTO();
        orderItemDTO = new OrderItemDTO(100.0F, 1L, 30);
        ordersDTO.setOrderItems(orderItemDTO);
        ordersDTOSend.setOrdersDTO(ordersDTO);

        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.reAddStock(ordersDTOSend))
                .expectNext(ordersDTOSend)
                .verifyComplete();
    }

    @Test
    void deleteByIdOk() {
        when(productRepository.deleteById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteById(1L))
                .verifyComplete();
    }

    @Test
    void deleteAllOk() {
        when(productRepository.deleteAll()).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteAll())
                .verifyComplete();
    }
}