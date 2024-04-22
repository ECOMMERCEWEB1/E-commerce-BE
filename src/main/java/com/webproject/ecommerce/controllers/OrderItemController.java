package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.OrderItemDTO;
import com.webproject.ecommerce.entities.OrderItem;
import com.webproject.ecommerce.repositories.OrderItemRepository;
import com.webproject.ecommerce.services.OrderItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.webproject.ecommerce.entities.OrderItem}.
 */
@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final Logger log = LoggerFactory.getLogger(OrderItemController.class);

    private final OrderItemService orderItemService;

    private final OrderItemRepository orderItemRepository;

    public OrderItemController(OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * {@code POST  /order-items} : Create a new orderItem.
     *
     * @param orderItem the orderItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderItem,
     * or with status {@code 400 (Bad Request)} if the orderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderItemDTO> createOrderItem(@Valid @RequestBody OrderItem orderItem) throws URISyntaxException {
        log.debug("REST request to save OrderItem : {}", orderItem);
        if (orderItem.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderItemDTO(orderItem, "OrderItem already has an Id !"));
        }
        OrderItem result = orderItemService.save(orderItem);
        return ResponseEntity
                .created(new URI("/api/order-items/" + result.getId()))
                .body(new OrderItemDTO(orderItem, "order item created successfully !"));

    }

    /**
     * {@code PUT  /order-items/:id} : Updates an existing orderItem.
     *
     * @param id the id of the orderItem to save.
     * @param orderItem the orderItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItem,
     * or with status {@code 400 (Bad Request)} if the orderItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody OrderItem orderItem
    ) {
        log.debug("REST request to update OrderItem : {}, {}", id, orderItem);
        if (orderItem.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderItemDTO(orderItem, "Invalid ID (Null value) !"));
        }
        if (!orderItemRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderItemDTO(orderItem, "ID does not exist !"));
        }

        if (!Objects.equals(id, orderItem.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderItemDTO(orderItem, "Invalid ID, query parameter ID:"+id+" != order item's Id:"+orderItem.getId()+" !"));
        }

        OrderItem result = orderItemService.update(orderItem);
        return ResponseEntity
                .ok()
                .body(new OrderItemDTO(result,"Order Item updated successfully !"));
    }

    /**
     * {@code PATCH  /order-items/:id} : Partial updates given fields of an existing orderItem, field will ignore if it is null
     *
     * @param id the id of the orderItem to save.
     * @param orderItem the orderItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItem,
     * or with status {@code 400 (Bad Request)} if the orderItem is not valid,
     * or with status {@code 404 (Not Found)} if the orderItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderItemDTO> partialUpdateOrderItem(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody OrderItem orderItem
    ){
        log.debug("REST request to partial update OrderItem partially : {}, {}", id, orderItem);
        if (orderItem.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderItemDTO(orderItem, "Invalid ID (Null value) !"));
        }
        if (!Objects.equals(id, orderItem.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderItemDTO(orderItem, "Invalid ID, query parameter ID:"+id+" != order item's Id:"+orderItem.getId()+" !"));
        }

        if (!orderItemRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderItemDTO(orderItem, "ID does not exist !"));
        }

        Optional<OrderItem> result = orderItemService.partialUpdate(orderItem);
        return result.map(item -> ResponseEntity
                .ok()
                .body(new OrderItemDTO(item, "Order Item updated successfully !")))
                .orElseGet(() -> ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new OrderItemDTO(orderItem, "Could not update the order item. Internal server error!")));

    }

    /**
     * {@code GET  /order-items} : get all the orderItems.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderItem>> getAllOrderItems(
            @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a list of OrderItems");
        List<OrderItem> list;
        if (eagerload) {
            list = orderItemService.findAllWithEagerRelationships();
        } else {
            list = orderItemService.findAll();
        }
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /order-items/:id} : get the "id" orderItem.
     *
     * @param id the id of the orderItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderItem,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderItem(@PathVariable("id") Long id) {
        log.debug("REST request to get OrderItem : {}", id);
        Optional<OrderItem> orderItem = orderItemService.findOne(id);
        return orderItem.map(item -> ResponseEntity
                .ok()
                .body(new OrderItemDTO(item,"Order item fetched successfully!")))
                .orElseGet(()-> ResponseEntity.internalServerError().body(new OrderItemDTO(null,"Internal server error! Could not fetch order item with id"+ id )));
    }

    /**
     * {@code DELETE  /order-items/:id} : delete the "id" orderItem.
     *
     * @param id the id of the orderItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete OrderItem : {}", id);
        if(orderItemService.findOne(id).isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Order item does not exist"));
        orderItemService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new MessageDTO("Order Item deleted Successfully!"));
    }
}

