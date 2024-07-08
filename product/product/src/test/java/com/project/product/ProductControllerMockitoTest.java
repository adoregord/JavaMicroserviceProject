// package com.project.product;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.when;

// import java.time.LocalDateTime;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.ResponseEntity;

// import com.project.product.exception.ProductException;

// import lombok.extern.slf4j.Slf4j;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;

// import static org.junit.jupiter.api.Assertions.*;

// @Slf4j
// public class ProductControllerMockitoTest {

//     @Mock
//     private ProductService productService;

//     @InjectMocks
//     private ProductController productController;

//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void findProductByIdOk() {
//         var product = Product.builder()
//                 .id(1L)
//                 .price(29.99f)
//                 .category("Category")
//                 .created_at(LocalDateTime.now())
//                 .description("Description")
//                 .image_url("ImageUrl")
//                 .stock_quantity(10)
//                 .published_at(LocalDateTime.now())
//                 .build();
//         log.info("product: {}", product);

//         when(productService.getById(anyLong())).thenReturn(Mono.just(product));

//         Mono<ResponseEntity<Product>> response = productController.getById(1L);
//         response.subscribe(
//                 value -> {
//                     var existProduct = value.getBody();
//                     log.info("exist product={}", existProduct);
//                     assertNotNull(existProduct);
//                     assertEquals(product.getId(), existProduct.getId());
//                     assertEquals(product.getPrice(), existProduct.getPrice());
//                 });
//     }

//     @Test
//     void findProductByIdErrorNotFound() {
//         var product = Product.builder()
//                 .id(1L)
//                 .price(29.99f)
//                 .category("Category")
//                 .created_at(LocalDateTime.now())
//                 .description("Description")
//                 .image_url("ImageUrl")
//                 .stock_quantity(10)
//                 .published_at(LocalDateTime.now())
//                 .build();

//         when(productService.getById(anyLong())).thenReturn(
//                 Mono.error(new ProductException(String.format("Product not found ID:%s ", product.getId()))));

//         Mono<ResponseEntity<Product>> response = productController.getById(2L);
//         response.subscribe(
//                 value -> {
//                     log.info("response={}", value);
//                     assertTrue(value.getStatusCode().is4xxClientError());
//                     assertNull(value.getBody());
//                 });
//     }

//     @Test
//     void findAllProductsOk() {
//         var product1 = Product.builder()
//                 .id(1L)
//                 .price(29.99f)
//                 .category("Category1")
//                 .created_at(LocalDateTime.now())
//                 .description("Description1")
//                 .image_url("ImageUrl1")
//                 .stock_quantity(10)
//                 .published_at(LocalDateTime.now())
//                 .build();
//         var product2 = Product.builder()
//                 .id(2L)
//                 .price(39.99f)
//                 .category("Category2")
//                 .created_at(LocalDateTime.now())
//                 .description("Description2")
//                 .image_url("ImageUrl2")
//                 .stock_quantity(20)
//                 .published_at(LocalDateTime.now())
//                 .build();

//         when(productService.getAllProduct()).thenReturn(Flux.just(product1, product2));

//         Flux<Product> response = productController.getAllProduct();
//         response.collectList().subscribe(
//                 value -> {
//                     log.info("response={}", value);
//                     assertEquals(2, value.size());
//                     assertEquals(product1.getId(), value.get(0).getId());
//                     assertEquals(product2.getId(), value.get(1).getId());
//                 }
//         );
//     }

//     @Test
//     void addProductOk() {
//         var product = Product.builder()
//                 .id(1L)
//                 .price(29.99f)
//                 .category("Category")
//                 .created_at(LocalDateTime.now())
//                 .description("Description")
//                 .image_url("ImageUrl")
//                 .stock_quantity(10)
//                 .published_at(LocalDateTime.now())
//                 .build();

//         when(productService.addProduct(any(Product.class))).thenReturn(Mono.just(product));

//         Mono<ResponseEntity<Product>> response = productController.addProduct(product);
//         response.subscribe(
//                 value -> {
//                     var addedProduct = value.getBody();
//                     log.info("added product={}", addedProduct);
//                     assertNotNull(addedProduct);
//                     assertEquals(product.getId(), addedProduct.getId());
//                     assertEquals(product.getPrice(), addedProduct.getPrice());
//                 });
//     }

//     @Test
//     void addProductError() {
//         var product = Product.builder()
//                 .id(1L)
//                 .price(29.99f)
//                 .category("Category")
//                 .created_at(LocalDateTime.now())
//                 .description("Description")
//                 .image_url("ImageUrl")
//                 .stock_quantity(10)
//                 .published_at(LocalDateTime.now())
//                 .build();

//         when(productService.addProduct(any(Product.class))).thenReturn(Mono.empty());

//         Mono<ResponseEntity<Product>> response = productController.addProduct(product);
//         response.subscribe(
//                 value -> {
//                     log.info("response={}", value);
//                     assertTrue(value.getStatusCode().is4xxClientError());
//                 });
//     }

//     @Test
//     void updateProductOk() {
//         var product = Product.builder()
//                 .id(1L)
//                 .price(29.99f)
//                 .category("Category")
//                 .created_at(LocalDateTime.now())
//                 .description("Description")
//                 .image_url("ImageUrl")
//                 .stock_quantity(10)
//                 .published_at(LocalDateTime.now())
//                 .build();

//         when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(Mono.just(product));

//         Mono<ResponseEntity<Product>> response = productController.updateProduct(1L, product);
//         response.subscribe(
//                 value -> {
//                     var updatedProduct = value.getBody();
//                     log.info("updated product={}", updatedProduct);
//                     assertNotNull(updatedProduct);
//                     assertEquals(product.getId(), updatedProduct.getId());
//                     assertEquals(product.getPrice(), updatedProduct.getPrice());
//                 });
//     }

//     @Test
//     void updateProductError() {
//         var product = Product.builder()
//                 .id(1L)
//                 .price(29.99f)
//                 .category("Category")
//                 .created_at(LocalDateTime.now())
//                 .description("Description")
//                 .image_url("ImageUrl")
//                 .stock_quantity(10)
//                 .published_at(LocalDateTime.now())
//                 .build();

//         when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(Mono.empty());

//         Mono<ResponseEntity<Product>> response = productController.updateProduct(1L, product);
//         response.subscribe(
//                 value -> {
//                     log.info("response={}", value);
//                     assertTrue(value.getStatusCode().is4xxClientError());
//                 });
//     }

//     @Test
//     void deleteProductByIdOk() {
//         when(productService.deleteById(anyLong())).thenReturn(Mono.empty());

//         Mono<Void> response = productController.deleteById(1L);
//         response.subscribe(
//                 value -> {
//                     log.info("response={}", value);
//                     assertNull(value);
//                 });
//     }

//     @Test
//     void deleteProductByIdError() {
//         when(productService.deleteById(anyLong())).thenReturn(
//                 Mono.error(new ProductException("Product deletion error")));

//         Mono<Void> response = productController.deleteById(1L);
//         response.subscribe(
//                 value -> {
//                     log.info("response={}", value);
//                     assertNotNull(value);
//                 }, 
//                 error -> {
//                     log.info("error={}", error.getMessage());
//                     assertEquals("Product deletion error", error.getMessage());
//                 });
//     }

//     @Test
//     void deleteAllProductsOk() {
//         when(productService.deleteAll()).thenReturn(Mono.empty());

//         Mono<Void> response = productController.deleteAll();
//         response.subscribe(
//                 value -> {
//                     log.info("response={}", value);
//                     assertNull(value);
//                 });
//     }

//     @Test
//     void deleteAllProductsError() {
//         when(productService.deleteAll()).thenReturn(
//                 Mono.error(new ProductException("Error deleting all products")));

//         Mono<Void> response = productController.deleteAll();
//         response.subscribe(
//                 value -> {
//                     log.info("response={}", value);
//                     assertNotNull(value);
//                 }, 
//                 error -> {
//                     log.info("error={}", error.getMessage());
//                     assertEquals("Error deleting all products", error.getMessage());
//                 });
//     }
// }
