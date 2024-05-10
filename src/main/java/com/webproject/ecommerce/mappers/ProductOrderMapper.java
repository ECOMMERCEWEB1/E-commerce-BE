package com.webproject.ecommerce.mappers;

import com.webproject.ecommerce.dto.ProductOrderDTO;
import com.webproject.ecommerce.entities.Invoice;
import com.webproject.ecommerce.entities.ProductOrder;
import com.webproject.ecommerce.entities.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductOrderMapper {

    @Mapping(target = "customer_id", source = "customer", qualifiedByName = "mapToUserId")
    @Mapping(target = "invoice_id", source = "invoice", qualifiedByName = "mapToInvoiceId")
    ProductOrderDTO toDto(ProductOrder productOrder, @Context String message);

    @Named("mapToUserId")
    default Long mapToUserId(User customer){
        return customer.getId();
    }

    @Named("mapToInvoiceId")
    default Long mapToInvoiceId(Invoice invoice){
        return invoice.getId();
    }

    @Mapping(target = "customer", source = "customer_id", qualifiedByName = "mapToUser")
    @Mapping(target = "invoice", source = "invoice_id", qualifiedByName = "mapToInvoice")
    ProductOrder toEntity(ProductOrderDTO productOrderDTO);

    @Named("mapToUser")
    default User mapToUser(Long customerId) {
        if (customerId == null) {
            return null;
        }
        User user = new User();
        user.setId(customerId);
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
