package com.project.payment.payment;

import org.springframework.stereotype.Repository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

@Repository
public interface PaymentRepository extends R2dbcRepository<Payment, Long>{
    
}
