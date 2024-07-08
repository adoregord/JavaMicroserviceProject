package com.example.DTO.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    
    @NotNull
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Float amount;

    @NotNull
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Long order_id;
    
    @NotNull
    private String mode;
    @NotNull
    private String status;
    @NotNull
    private String reference_number;

}
