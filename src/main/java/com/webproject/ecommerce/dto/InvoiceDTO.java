package com.webproject.ecommerce.dto;

import com.webproject.ecommerce.enums.InvoiceStatus;
import com.webproject.ecommerce.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceDTO {
    private PaymentMethod paymentMethod;
    private InvoiceStatus invoiceStatus;
}
