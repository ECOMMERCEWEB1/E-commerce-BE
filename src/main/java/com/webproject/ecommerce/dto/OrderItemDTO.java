package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.entities.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDTO {
    private OrderItem orderItem;
    private String message;
}
