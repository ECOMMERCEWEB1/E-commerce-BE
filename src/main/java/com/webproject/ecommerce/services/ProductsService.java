package com.webproject.ecommerce.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.enums.Brand;
import com.webproject.ecommerce.enums.OrderItemStatus;
import com.webproject.ecommerce.repositories.ProductsRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductsService {
    private final ProductsRepository productRepository;

    private final Logger log = LoggerFactory.getLogger(ProductsService.class);

    public ProductsService(ProductsRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Optional<Product> getProductByName(String productName){
        return productRepository.findByName(productName);
    }

    public Optional<List<Product>> getProductsByBrand(Brand brand){
        return productRepository.findByBrand(brand);
    }

    public Optional<List<Product>> getProductsByStatus(OrderItemStatus orderItemStatus){
        return productRepository.findByStatus(orderItemStatus);
    }

    public Optional<List<Product>> getProductsByPriceGreaterThan(double price){
        return productRepository.findByPriceGreaterThan(price);
    }

    public Optional<List<Product>> getProductsByPriceLessThan(double price){
        return productRepository.findByPriceLessThan(price);
    }

    public Optional<List<Product>> getProductsByBrandAndStatus(Brand brand, OrderItemStatus orderItemStatus){
        return productRepository.findByBrandAndStatus(brand, orderItemStatus);
    }

    public Optional<List<Product>> getProductsByDescriptionContaining(String keyword){
        return productRepository.findByDescriptionContaining(keyword);
    }

    public Product createProduct(Product product)
    {
        return productRepository.save(product);
    }

    /**
     * Update a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    public Product updateProduct(Product product) {
        log.debug("Request to update Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Partially update a product.
     *
     * @param product the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
                .findById(product.getId())
                .map(existingProduct -> {
                    if (product.getName() != null) {
                        existingProduct.setName(product.getName());
                    }
                    if (product.getDescription() != null) {
                        existingProduct.setDescription(product.getDescription());
                    }
                    if (product.getPrice() != -1) {
                        existingProduct.setPrice(product.getPrice());
                    }

                    return existingProduct;
                })
                .map(productRepository::save);
    }



    public void deleteProduct(Long id)
    {
        productRepository.deleteById(id);
    }

    public boolean productNameExists(String Name)
    {
        return productRepository.findByName(Name).isPresent();

    }

    public boolean productIdExists(Long id)
    {
        return productRepository.findById(id).isPresent();
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }
} 

