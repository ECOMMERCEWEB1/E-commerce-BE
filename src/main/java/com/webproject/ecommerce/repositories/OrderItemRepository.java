package com.webproject.ecommerce.repositories;

import com.webproject.ecommerce.entities.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllWithEagerRelationships();

    Optional<OrderItem> findOneWithEagerRelationships(Long id);
}