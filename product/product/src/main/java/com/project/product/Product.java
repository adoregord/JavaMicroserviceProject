package com.project.product;

import java.time.LocalDateTime;

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
public class Product {

    @Id
    private Long id;

    @NotNull
    private Float price;
    @NotNull
    private String category;

    private LocalDateTime created_at;
    @NotNull
    private String description;
    @NotNull
    private String image_url;
    @NotNull
    @Min(value = 0, message = "Stock quantity should not be less than 0")
    private Integer stock_quantity;

    private LocalDateTime published_at;


    // public Product(Long id) {
    //     this.id = id;
    // }

}
