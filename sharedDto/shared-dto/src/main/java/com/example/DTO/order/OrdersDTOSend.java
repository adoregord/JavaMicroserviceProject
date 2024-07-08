package com.example.DTO.order;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTOSend {

    private Long id;
    private OrdersDTO ordersDTO;
    private LocalDateTime order_date;

}
