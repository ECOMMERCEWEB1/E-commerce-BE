package com.webproject.ecommerce.repositories;

import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.enums.Brand;
import com.webproject.ecommerce.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

    List<Product> findAll();

    Optional<Product> findByName(String productName);

    Optional<List<Product>> findByBrand(Brand brand);

    Optional<List<Product>> findByStatus(Status status);

    Optional<List<Product>> findByPriceGreaterThan(double price);

    Optional<List<Product>> findByPriceLessThan(double price);

    Optional<List<Product>> findByBrandAndStatus(Brand brand, Status status);

    Optional<List<Product>> findByDescriptionContaining(String keyword);
}
