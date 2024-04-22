package com.webproject.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.webproject.ecommerce.enums.Brand;

import com.webproject.ecommerce.enums.OrderItemStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message="each product should have a name")
    @Length(min=2,message = "Product name must contain at least 2 characters")
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private ProductCategory productCategory;

    @NotNull
    @Column(name = "price")
    private double price;

    @NotNull(message="each product should have a brand")
    private Brand brand;

    @NotNull(message="each product should have a status")
    private OrderItemStatus status;
}
