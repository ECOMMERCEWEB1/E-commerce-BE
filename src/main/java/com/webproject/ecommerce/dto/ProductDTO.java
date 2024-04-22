package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.enums.Brand;
import com.webproject.ecommerce.enums.OrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Brand brand;
    private OrderItemStatus status;
    private String message;
}
