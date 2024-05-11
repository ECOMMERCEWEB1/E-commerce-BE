package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.entities.OrderItem;
import com.webproject.ecommerce.enums.OrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Integer quantity;
    private BigDecimal totalPrice;
    private OrderItemStatus status;
    private Long product_id;
}
