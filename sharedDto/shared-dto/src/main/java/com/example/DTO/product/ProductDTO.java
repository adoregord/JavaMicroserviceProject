package com.example.DTO.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotNull(message = "Product price cannot be null")
    @Min(value = 1, message = "Price should not be less than 1")
    private Float price;

    @NotBlank(message = "Product category cannot be blank")
    private String category;

    @NotBlank(message = "Product description cannot be blank")
    private String description;

    @NotNull(message = "Product image url cannot be null")
    @Size(max = 255, message = "Product image url cannot be more than 255 characters")
    private String image_url;

    @NotNull(message = "Product Stock cannot be null")
    @Min(value = 0, message = "Stock quantity should not be less than 0")
    private Integer stock_quantity;

}
