package com.webproject.ecommerce.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import com.webproject.ecommerce.dto.ProductOrderDTO;
import com.webproject.ecommerce.entities.Invoice;
import com.webproject.ecommerce.entities.OrderItem;
import com.webproject.ecommerce.entities.ProductOrder;
import com.webproject.ecommerce.enums.InvoiceStatus;
import com.webproject.ecommerce.enums.OrderStatus;
import com.webproject.ecommerce.repositories.InvoiceRepository;
import com.webproject.ecommerce.repositories.ProductOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.webproject.ecommerce.entities.ProductOrder}.
 */
@Service
@Transactional
public class ProductOrderService {

    private final Logger log = LoggerFactory.getLogger(ProductOrderService.class);

    private final ProductOrderRepository productOrderRepository;

    private final InvoiceRepository invoiceRepository;

    public ProductOrderService(ProductOrderRepository productOrderRepository,
    InvoiceRepository invoiceRepository) {
        this.productOrderRepository = productOrderRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Save a productOrder.
     *
     * @param productOrder the entity to save.
     * @return the persisted entity.
     */
    public ProductOrder save(ProductOrder productOrder) {
        log.debug("Request to save ProductOrder : {}", productOrder.getCustomer().getId());

        return productOrderRepository.save(setProductOrderDetails(productOrder));
    }
    private String generateCode() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
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
        return invoiceRepository.save(invoice);
    }
    private ProductOrder setProductOrderDetails(ProductOrder po){
        po.setCode(generateCode());
        po.setPlacedDate(Instant.now());
        po.setStatus(OrderStatus.PENDING);
        po.setInvoice(createInvoiceFromOrder(po));
        return po;
    }

    /**
     * Update a productOrder.
     *
     * @param productOrder the entity to save.
     * @return the persisted entity.
     */
    public ProductOrder update(ProductOrder productOrder) {
        log.debug("Request to update ProductOrder : {}", productOrder);
        return productOrderRepository.save(productOrder);
    }

    /**
     * Partially update a productOrder.
     *
     * @param productOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductOrder> partialOrderUpdate(ProductOrder productOrder) throws RuntimeException {
        log.debug("Request to partially update ProductOrder : {}", productOrder);

        return productOrderRepository
                .findById(productOrder.getId())
                .map(existingProductOrder -> {
                    if (productOrder.getStatus() == OrderStatus.CANCELLED) {
                        if(existingProductOrder.getInvoice().getStatus().equals(InvoiceStatus.PAID)) {
                            throw new RuntimeException("Order already paid for, can not cancel!");
                        }
                        existingProductOrder.setStatus(productOrder.getStatus());
                        existingProductOrder.getInvoice().status(InvoiceStatus.CANCELLED);
                    }
                    return existingProductOrder;
                })
                .map(productOrderRepository::save);
    }

    /**
     * Partially update an invoice.
     *
     * @param invoice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Invoice> partialInvoiceUpdate(Invoice invoice) throws RuntimeException {
        log.debug("Request to partially update Invoice : {}", invoice);

        return invoiceRepository
                .findById(invoice.getId())
                .map(existingInvoice -> {
                    if (existingInvoice.getStatus().equals(InvoiceStatus.PAID))
                        throw new RuntimeException("Order already paid for, can not update!");
                    else {
                        if (invoice.getStatus() != null) {
                            existingInvoice.setStatus(invoice.getStatus());
                            if (invoice.getStatus().equals(InvoiceStatus.PAID))
                                existingInvoice.setPaymentDate(Instant.now());
                        }
                        if (invoice.getDetails() != null)
                            existingInvoice.setDetails(invoice.getDetails());
                        if (invoice.getPaymentMethod() != null)
                            existingInvoice.setPaymentMethod(invoice.getPaymentMethod());
                    }
                    return existingInvoice;
                })
                .map(invoiceRepository::save);

    }


    /**
     * Get all the productOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOrder> findAll(Pageable pageable) {
        log.debug("Request to get all ProductOrders");
        return productOrderRepository.findAll(pageable);
    }

    /**
     * Get one productOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductOrder> findOne(Long id) {
        log.debug("Request to get ProductOrder : {}", id);
        return productOrderRepository.findById(id);
    }

    /**
     * Delete the productOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductOrder : {}", id);
        productOrderRepository.deleteById(id);
    }
    /**
     * Count all the product Orders.
     *
     *  @return the number of product orders.
     */
    public Long count(){
        return productOrderRepository.count();
    }

    @Transactional
    public ProductOrder updateInvoiceForOrder(Long orderId, Invoice invoiceData) {
        ProductOrder order = productOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Product Order not found with id: " + orderId));
        Invoice invoice = order.getInvoice();
        invoice.setCode(invoiceData.getCode());
        invoice.setDate(invoiceData.getDate());
        invoice.setDetails(invoiceData.getDetails());
        invoice.setStatus(invoiceData.getStatus());
        invoice.setPaymentMethod(invoiceData.getPaymentMethod());
        invoice.setPaymentDate(invoiceData.getPaymentDate());
        invoice.setPaymentAmount(invoiceData.getPaymentAmount());
        invoice = invoiceRepository.save(invoice);
        order.setInvoice(invoice);

        return productOrderRepository.save(order);
    }
}


