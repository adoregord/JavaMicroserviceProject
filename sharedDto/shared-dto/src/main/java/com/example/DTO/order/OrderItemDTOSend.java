package com.example.DTO.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTOSend {

    @NotNull(message = "Product ID cannot be null")
    private Long product_id;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

}
