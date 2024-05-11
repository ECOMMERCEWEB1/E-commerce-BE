package com.webproject.ecommerce.mappers;

import com.webproject.ecommerce.dto.OrderItemDTO;
import com.webproject.ecommerce.dto.ProductOrderDTO;
import com.webproject.ecommerce.entities.Invoice;
import com.webproject.ecommerce.entities.OrderItem;
import com.webproject.ecommerce.entities.ProductOrder;
import com.webproject.ecommerce.entities.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

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

    Set<OrderItemDTO> mapToOrderItemDTOs(Set<OrderItem> orderItems);

    default OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        OrderItemDTO dto = new OrderItemDTO();
        dto.setQuantity(orderItem.getQuantity());
        dto.setTotalPrice(orderItem.getTotalPrice());
        dto.setStatus(orderItem.getStatus());
        dto.setProduct_id(orderItem.getProduct().getId());
        return dto;
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
