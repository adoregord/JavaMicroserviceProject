package com.project.order.orderItem;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    private Long id;

    private Float price;

    @NotNull
    @Min(value = 1, message = "Product Id must be greater than or equal to 1")
    private Long product_id;

    @NotNull
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Integer quantity;

    @NotNull
    private Long order_id;

}
