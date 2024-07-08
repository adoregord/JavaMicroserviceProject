package com.project.product;

import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.order.OrdersDTOSend;
import com.example.DTO.product.ProductDTO;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/inventory")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    // display all product
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    // add product
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Product>> addProduct2(@Valid @RequestBody ProductDTO product) {
        return productService.addProduct(product)
                .map(product2 -> ResponseEntity.status(HttpStatus.CREATED).body(product2))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    // get product by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Product>> getById(@Valid @PathVariable Long id) {
        return productService.getById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // update product
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@Valid @PathVariable Long id,
            @Valid @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(productUpdate -> ResponseEntity.status(HttpStatus.CREATED).body(productUpdate));
    }

    // update the database to add the amount of stock quantity in db
    @PatchMapping("/add/{id}")
    public Mono<ResponseEntity<Product>> addStockQuantity(@Valid @PathVariable Long id, @RequestParam Integer sumItem) {
        return productService.addStockQuantity(id, sumItem)
                .map(productUpdate -> ResponseEntity.status(HttpStatus.OK).body(productUpdate))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // check the product and deduct the product stock
    @PostMapping("/check-deduct-stock")
    public Mono<ResponseEntity<OrdersDTOSend>> checkAndDeductStock(@Valid @RequestBody OrdersDTOSend ordersDTO) {
        log.info("Inside checkAndDeductStock method of ProductController: " + ordersDTO.toString());
        return productService.checkAndDeductStock(ordersDTO)
                .map(productUpdate -> ResponseEntity.status(HttpStatus.OK).body(productUpdate))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/re-add-stock")
    public Mono<ResponseEntity<OrdersDTOSend>> reAddStock(@Valid @RequestBody OrdersDTOSend ordersDTO) {
        log.info("Inside reAddStock method of ProductController: " + ordersDTO.toString());
        return productService.reAddStock(ordersDTO)
                .map(productUpdate -> ResponseEntity.status(HttpStatus.OK).body(productUpdate))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // delete the product by id
    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@Valid @PathVariable Long id) {
        return productService.deleteById(id);
    }

    // delete all products
    @DeleteMapping
    public Mono<Void> deleteAll() {
        return productService.deleteAll();
    }

}

// @PostMapping("/add")
// @ResponseStatus(HttpStatus.CREATED)
// public Mono<ResponseEntity<Product>> addProduct(@Valid @RequestBody Product
// product) {
// return productService.addProduct(product)
// .map(product1 -> ResponseEntity.status(HttpStatus.CREATED).body(product1))
// .defaultIfEmpty(ResponseEntity.badRequest().build());
// }