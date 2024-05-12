package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.entities.Invoice;
import com.webproject.ecommerce.enums.InvoiceStatus;
import com.webproject.ecommerce.enums.PaymentMethod;
import com.webproject.ecommerce.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(value="http://localhost:4200",allowCredentials = "true")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok().body(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        return invoice.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) throws URISyntaxException {
        if (invoice.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
        return ResponseEntity.created(new URI("/api/invoices/" + savedInvoice.getId())).body(savedInvoice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoice) {
        if (!invoiceService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        invoice.setId(id);
        Invoice updatedInvoice = invoiceService.saveInvoice(invoice);
        return ResponseEntity.ok().body(updatedInvoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        if (!invoiceService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
