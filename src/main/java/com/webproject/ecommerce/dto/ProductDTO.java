package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.entities.Product;

import com.webproject.ecommerce.enums.Brand;
import com.webproject.ecommerce.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Brand brand;
    private Status status;
    private String message;
}
