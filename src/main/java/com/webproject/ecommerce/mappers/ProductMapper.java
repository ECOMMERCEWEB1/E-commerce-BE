package com.webproject.ecommerce.mappers;

import com.webproject.ecommerce.dto.ProductDTO;
import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.entities.ProductCategory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product, String message);

    // Mapping method for ProductCategory to String
    default String mapProductCategoryToString(ProductCategory productCategory) {
        return productCategory != null ? productCategory.getName() : null;
    }
}
