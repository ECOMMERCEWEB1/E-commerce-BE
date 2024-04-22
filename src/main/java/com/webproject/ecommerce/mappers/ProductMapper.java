package com.webproject.ecommerce.mappers;

import com.webproject.ecommerce.dto.ProductDTO;
import com.webproject.ecommerce.entities.Product;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDto(Product product, @Context String message);

    //Product toEntity(ProductDTO productDTO);
}
