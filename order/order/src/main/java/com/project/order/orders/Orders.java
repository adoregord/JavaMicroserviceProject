package com.project.order.orders;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    @Id
    private Long id;

    @NotBlank
    private String billing_address;

    @NotNull
    @Min(value = 1, message = "Customer ID must be greater than or equal to 1")
    private Long customer_id;

    private LocalDateTime order_date;
    
    private String order_status;
    @NotNull
    private String payment_method;
    @NotNull
    private String shipping_address;
    @NotNull
    private Float total_amount;

    // public Orders(String billing_address, Long customer_id, String order_status,
    //         String payment_method, String shipping_address, Float total_amount) {
    //     this.billing_address = billing_address;
    //     this.customer_id = customer_id;
    //     this.order_status = order_status;
    //     this.payment_method = payment_method;
    //     this.shipping_address = shipping_address;
    //     this.total_amount = total_amount;
    // }
}
