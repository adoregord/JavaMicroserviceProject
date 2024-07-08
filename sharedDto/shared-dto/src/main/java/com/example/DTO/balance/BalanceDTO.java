package com.example.DTO.balance;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {

    @NotNull
    @Min(value = 0, message = "Balance amount must be greater than or equal to 0")
    private Float amount;

}
