package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {
    private Product product;
    private String message;

}
