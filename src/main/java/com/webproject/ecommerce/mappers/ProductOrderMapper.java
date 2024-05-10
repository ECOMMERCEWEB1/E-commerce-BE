package com.webproject.ecommerce.mappers;

import com.webproject.ecommerce.dto.ProductOrderDTO;
import com.webproject.ecommerce.entities.Invoice;
import com.webproject.ecommerce.entities.ProductOrder;
import com.webproject.ecommerce.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductOrderMapper {

    ProductOrderDTO toDto(ProductOrder productOrder, String message);

    @Mapping(target = "customer", source = "customer_id", qualifiedByName = "mapToUser")
    @Mapping(target = "invoice", source = "invoice_id", qualifiedByName = "mapToInvoice")
    ProductOrder toEntity(ProductOrderDTO productOrderDTO);

    @Named("mapToUser")
    default User mapToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("mapToInvoice")
    default Invoice mapToInvoice(Long invoiceId) {
        if (invoiceId == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        return invoice;
    }

}
