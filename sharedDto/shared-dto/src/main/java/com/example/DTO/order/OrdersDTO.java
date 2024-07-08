package com.example.DTO.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {

    @NotBlank(message = "Billing address cannot be blank")
    private String billing_address;

    @Min(value = 1, message = "Customer ID must be greater than or equal to 1")
    @NotNull(message = "Customer ID cannot be null")
    private Long customer_id;

    @NotBlank(message = "Order status cannot be blank")
    private String order_status;

    @NotBlank(message = "Payment method cannot be blank")
    private String payment_method;

    @NotBlank(message = "Shipping address cannot be blank")
    private String shipping_address;

    private Float total_amount;

    @NotNull(message = "Order items cannot be null")
    @Valid
    private OrderItemDTO orderItems;

}
