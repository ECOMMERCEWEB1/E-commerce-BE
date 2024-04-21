package com.webproject.ecommerce.services;

import java.util.List;
import java.util.Optional;

import com.webproject.ecommerce.repositories.OrderItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.webproject.ecommerce.entities.OrderItem;

/**
 * Service Implementation for managing {@link com.webproject.ecommerce.entities.OrderItem}.
 */
@Service
@Transactional
public class OrderItemService {

    private final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Save a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public OrderItem save(OrderItem orderItem) {
        log.debug("Request to save OrderItem : {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    /**
     * Update a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public OrderItem update(OrderItem orderItem) {
        log.debug("Request to update OrderItem : {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    /**
     * Partially update a orderItem.
     *
     * @param orderItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItem> partialUpdate(OrderItem orderItem) {
        log.debug("Request to partially update OrderItem : {}", orderItem);

        return orderItemRepository
                .findById(orderItem.getId())
                .map(existingOrderItem -> {
                    if (orderItem.getQuantity() != null) {
                        existingOrderItem.setQuantity(orderItem.getQuantity());
                    }
                    if (orderItem.getTotalPrice() != null) {
                        existingOrderItem.setTotalPrice(orderItem.getTotalPrice());
                    }
                    if (orderItem.getStatus() != null) {
                        existingOrderItem.setStatus(orderItem.getStatus());
                    }

                    return existingOrderItem;
                })
                .map(orderItemRepository::save);
    }

    /**
     * Get all the orderItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderItem> findAll() {
        log.debug("Request to get all OrderItems");
        return orderItemRepository.findAll();
    }

    /**
     * Get all the orderItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public List<OrderItem> findAllWithEagerRelationships() {
        return orderItemRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one orderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItem> findOne(Long id) {
        log.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderItem : {}", id);
        orderItemRepository.deleteById(id);
    }
}
