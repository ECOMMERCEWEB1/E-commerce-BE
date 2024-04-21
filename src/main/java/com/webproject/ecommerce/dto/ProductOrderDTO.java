package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.entities.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductOrderDTO {
    private ProductOrder productOrder;
    private String message;
}