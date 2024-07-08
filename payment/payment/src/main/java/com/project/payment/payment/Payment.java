package com.project.payment.payment;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("transaction_details")
public class Payment {

    @Id
    private Long id;

    @NotNull
    private Float amount;
    @NotNull
    private Long order_id;

    private LocalDateTime payment_date;
    @NotNull
    private String mode;
    @NotNull
    private String status;
    @NotNull
    private String reference_number;

}
