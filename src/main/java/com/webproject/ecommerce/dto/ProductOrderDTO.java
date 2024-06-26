package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.entities.OrderItem;
import java.time.Instant;
import com.webproject.ecommerce.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProductOrderDTO {
    private Long id;
    private Set<OrderItemDTO> orderItems;
    private String message;
    private Long customer_id;
    private String code;
    private OrderStatus status;
    private Instant placedDate;
    private Long invoice_id;

}