package com.bookstore.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotificationMessage {

    private Long orderId;
    private Long userId;
    private Long bookId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;
    private String message;
}