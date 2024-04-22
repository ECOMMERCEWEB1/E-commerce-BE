package com.webproject.ecommerce.repositories;

import com.webproject.ecommerce.entities.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @EntityGraph(attributePaths = {"product"})
    List<OrderItem> findAll();

    @EntityGraph(attributePaths = {"product"})
    Optional<OrderItem> findById(Long id);


}