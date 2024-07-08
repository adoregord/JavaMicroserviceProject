package com.example.DTO.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Float price;

    @NotNull(message = "Product ID cannot be null")
    @Min(value = 1, message = "Product Id must be greater than or equal to 1")
    private Long product_id;

    @Valid
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Integer quantity;

}
