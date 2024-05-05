package com.webproject.ecommerce.entities.listeners;

import com.webproject.ecommerce.entities.Invoice;
import com.webproject.ecommerce.entities.ProductOrder;
import com.webproject.ecommerce.entities.OrderItem;
import com.webproject.ecommerce.enums.InvoiceStatus;
import com.webproject.ecommerce.enums.PaymentMethod;
import com.webproject.ecommerce.repositories.InvoiceRepository;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.math.BigDecimal;
import java.time.Instant;

public class OrderEntityListener {

    private final InvoiceRepository invoiceRepository;

    public OrderEntityListener(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @PrePersist
    @PreUpdate
    public void beforePersistOrUpdate(ProductOrder order) {
        if (order.getInvoice() == null) {
            Invoice invoice = createInvoiceFromOrder(order);
            order.setInvoice(invoice);
            invoiceRepository.save(invoice); // Save the invoice
        }
    }

    private Invoice createInvoiceFromOrder(ProductOrder order) {
        Invoice invoice = new Invoice();
        // Set invoice properties based on order and its items
        // For simplicity, let's assume some values
        invoice.setCode("INV-" + order.getCode());
        invoice.setDate(Instant.now());
        invoice.setStatus(InvoiceStatus.ISSUED);
        // Calculate total price from order items
        BigDecimal totalPrice = order.getOrderItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice.setPaymentAmount(totalPrice);
        return invoice;
    }
}

