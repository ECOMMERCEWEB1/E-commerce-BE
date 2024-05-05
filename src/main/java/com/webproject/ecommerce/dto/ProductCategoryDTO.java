package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.entities.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String message;
}
