package com.webproject.ecommerce.mappers;


import com.webproject.ecommerce.dto.ProductCategoryDTO;
import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.entities.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    ProductCategoryDTO toDto(ProductCategory category, String message);
}
