package com.project.order.orders;

import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.order.OrdersDTO;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/all") // display all orders
    public Flux<Orders> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @GetMapping("/{id}") // display order by id
    public Mono<ResponseEntity<Orders>> getOrdersById(@PathVariable Long id) {
        return ordersService.getOrdersById(id)
                .map(orders -> ResponseEntity.ok(orders))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create") // add orders
    public Mono<ResponseEntity<Object>> addOrders(@Valid @RequestBody OrdersDTO orders) {
        return ordersService.addOrders(orders)
                .map(ordersAdd -> ResponseEntity.status(HttpStatus.CREATED).body(ordersAdd))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }

    @PutMapping("/update/{id}") // update order by id
    public Mono<ResponseEntity<Orders>> updateOrders(@PathVariable Long id, @RequestBody Orders orders) {
        return ordersService.updateOrders(id, orders)
                .map(ordersUpdate -> ResponseEntity.status(HttpStatus.CREATED).body(ordersUpdate))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // delete order item by id
    public Mono<Void> deleteOrdersById(@PathVariable Long id) {
        return ordersService.deleteOrdersById(id);
    }

    @DeleteMapping // delete all order items
    public Mono<Void> deleteAllOrders() {
        return ordersService.deleteAllOrders();
    }

}
