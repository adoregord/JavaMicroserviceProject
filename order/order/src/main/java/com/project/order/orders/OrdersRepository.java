package com.project.order.orders;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrdersRepository extends R2dbcRepository<Orders, Long>{
    
    

}
