package com.project.order.orderItem;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long>{
    
}
