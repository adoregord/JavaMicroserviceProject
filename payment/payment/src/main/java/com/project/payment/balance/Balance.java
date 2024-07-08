package com.project.payment.balance;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance {
    
    @Id
    private Long id;

    @NotNull
    private Float amount;

    @NotNull
    private Long customer_id;

}
