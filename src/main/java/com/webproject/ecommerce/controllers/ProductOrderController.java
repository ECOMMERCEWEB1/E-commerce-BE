package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.ProductOrderDTO;
import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.entities.Invoice;
import com.webproject.ecommerce.entities.ProductOrder;
import com.webproject.ecommerce.entities.User;
import com.webproject.ecommerce.mappers.ProductOrderMapper;
import com.webproject.ecommerce.repositories.ProductOrderRepository;
import com.webproject.ecommerce.repositories.UsersRepository;
import com.webproject.ecommerce.services.ProductOrderService;
import com.webproject.ecommerce.services.ProductsService;
import com.webproject.ecommerce.services.UsersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.webproject.ecommerce.entities.ProductOrder}.
 */
@RestController
@RequestMapping("/api/product-orders")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials= "true")
public class ProductOrderController {

    private final Logger log = LoggerFactory.getLogger(ProductOrderController.class);

    private final ProductOrderService productOrderService;

    private final ProductOrderRepository productOrderRepository;

    private final ProductOrderMapper productOrderMapper;

    private final UsersService usersRepository;

    private final ProductsService productsService;

    public ProductOrderController(ProductsService productsService,UsersService usersRepository,ProductOrderMapper productOrderMapper,ProductOrderService productOrderService, ProductOrderRepository productOrderRepository) {
        this.productOrderService = productOrderService;
        this.productOrderRepository = productOrderRepository;
        this.productOrderMapper = productOrderMapper;
        this.usersRepository = usersRepository;
        this.productsService = productsService;
    }

    /**
     * {@code POST  /product-orders} : Create a new productOrder.
     *
     * @param productOrder the productOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productOrder, or with status {@code 400 (Bad Request)} if the productOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductOrderDTO> createProductOrder(@Valid @RequestBody ProductOrderDTO productOrder) throws URISyntaxException {
         log.debug("REST request to save ProductOrder : {}", productOrder.getOrderItems());
        if (productOrder.getId() != null) {
            productOrder.setMessage("Product Order already has an Id !");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productOrder);
        }
        ProductOrder mappedProductOrder = productOrderMapper.toEntity(productOrder);
        mappedProductOrder.getOrderItems().stream().map(orderItem ->{
            if (!productsService.productIdExists(orderItem.getProduct().getId())){
                productOrder.setMessage("Product with id : "+orderItem.getProduct().getId()+" doesn't exist!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productOrder);

            }
            Optional<Product> product = productsService.findOne(orderItem.getId());
                orderItem.setProduct(product.get());
            return orderItem;
        });

        User user = this.usersRepository.getUserById(mappedProductOrder.getCustomer().getId());
        if (user==null){
            productOrder.setMessage("Customer with id : "+productOrder.getCustomer_id()+" doesn't exist!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productOrder);
        }
        mappedProductOrder.setCustomer(user);

        ProductOrder result = productOrderService.save(mappedProductOrder);
        return ResponseEntity
                .created(new URI("/api/product-orders/" + result.getId()))
                .body(productOrderMapper.toDto(result, "product order created successfully !"));

    }


    /**
     * {@code PUT  /product-orders/:id} : Updates an existing productOrder.
     *
     * @param id the id of the productOrder to save.
     * @param productOrder the productOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOrder,
     * or with status {@code 400 (Bad Request)} if the productOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOrder couldn't be updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductOrderDTO> updateProductOrder(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody ProductOrder productOrder
    ){
        log.debug("REST request to update ProductOrder : {}, {}", id, productOrder);
        if (productOrder.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productOrderMapper.toDto(productOrder, "Invalid ID (Null value) !"));
        }
        if (!Objects.equals(id, productOrder.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productOrderMapper.toDto(productOrder, "Invalid ID, query parameter ID:"+id+" != product order's Id:"+productOrder.getId()+" !"));
        }
        if (!productOrderRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productOrderMapper.toDto(productOrder, "ID does not exist !"));
        }

        ProductOrder result = productOrderService.update(productOrder);
        return ResponseEntity
                .ok()
                .body(productOrderMapper.toDto(result, "Product Order created successfully!"));
    }

    /**
     * {@code PATCH  /product-orders/:id} : Partial updates given fields of an existing productOrder, field will ignore if it is null
     *
     * @param id the id of the productOrder to save.
     * @param productOrder the productOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOrder,
     * or with status {@code 400 (Bad Request)} if the productOrder is not valid,
     * or with status {@code 404 (Not Found)} if the productOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the productOrder couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductOrderDTO> partialUpdateProductOrder(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody ProductOrder productOrder
    ) {
        log.debug("REST request to partial update ProductOrder partially : {}, {}", id, productOrder);
        if (productOrder.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productOrderMapper.toDto(productOrder, "Invalid ID (Null value) !"));
        }
        if (!Objects.equals(id, productOrder.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productOrderMapper.toDto(productOrder, "Invalid ID, query parameter ID:" + id + " != product order's Id:" + productOrder.getId() + " !"));
        }

        if (!productOrderRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productOrderMapper.toDto(productOrder, "ID does not exist !"));
        }

        Optional<ProductOrder> result;
        try {
            result = productOrderService.partialOrderUpdate(productOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productOrderMapper.toDto(productOrder, e.getMessage()));
        }

        return result.map(order -> ResponseEntity
                        .ok()
                        .body(productOrderMapper.toDto(order, "Product Order updated successfully !")))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(productOrderMapper.toDto(productOrder, "Could not update the product order. Internal server error!")));
    }



    /**
     * {@code GET  /product-orders} : get all the productOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productOrders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductOrder>> getAllProductOrders(
            Pageable pageable
    ) {
        log.debug("REST request to get a page of ProductOrders");
        Page<ProductOrder> page = productOrderService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /product-orders/:id} : get the "id" productOrder.
     *
     * @param id the id of the productOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductOrder> getProductOrder(@PathVariable("id") Long id) {
        log.debug("REST request to get ProductOrder : {}", id);
        Optional<ProductOrder> productOrder = productOrderService.findOne(id);
        return ResponseEntity.of(productOrder);
    }

    /**
     * {@code DELETE  /product-orders/:id} : delete the "id" productOrder.
     *
     * @param id the id of the productOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductOrder(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProductOrder : {}", id);
        if(productOrderService.findOne(id).isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Product Order does not exist"));
        productOrderService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new MessageDTO("Product Order Deleted Successfully!"));
    }
    /**
     * {@code GET  /product-orders} : get the count of all the productOrders.
     * no params
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count of all the products.
     */
    @CrossOrigin(value = "http://localhost:4200",allowCredentials = "true")
    @GetMapping("/count")
    public ResponseEntity<Long> getAllProductOrders(
    ) {
        log.debug("REST request to get the number of ProductOrders");
        long count = productOrderService.count();
        return ResponseEntity.ok().body(count);
    }

    @PutMapping("/{orderId}/invoice")
    public ResponseEntity<ProductOrder> updateInvoiceForOrder(
            @PathVariable Long orderId,
            @RequestBody Invoice invoice) {
        ProductOrder updatedOrder = productOrderService.updateInvoiceForOrder(orderId, invoice);
        return ResponseEntity.ok(updatedOrder);
    }
}

