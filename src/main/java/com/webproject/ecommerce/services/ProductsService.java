package com.webproject.ecommerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.enums.Brand;
import com.webproject.ecommerce.enums.Status;
import com.webproject.ecommerce.repositories.ProductsRepository;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository){
        this.productsRepository = productsRepository;
    }

    public List<Product> getProducts(){
        return productsRepository.findAll();
    }

    public Optional<Product> getProductByName(String productName){
        return productsRepository.findByName(productName);
    }

    public Optional<List<Product>> getProductsByBrand(Brand brand){
        return productsRepository.findByBrand(brand);
    }

    public Optional<List<Product>> getProductsByStatus(Status status){
        return productsRepository.findByStatus(status);
    }

    public Optional<List<Product>> getProductsByPriceGreaterThan(double price){
        return productsRepository.findByPriceGreaterThan(price);
    }

    public Optional<List<Product>> getProductsByPriceLessThan(double price){
        return productsRepository.findByPriceLessThan(price);
    }

    public Optional<List<Product>> getProductsByBrandAndStatus(Brand brand, Status status){
        return productsRepository.findByBrandAndStatus(brand, status);
    }

    public Optional<List<Product>> getProductsByDescriptionContaining(String keyword){
        return productsRepository.findByDescriptionContaining(keyword);
    }

    public void createProduct(Product product)
    {
        productsRepository.save(product);
    }

    public Product updateProduct(Long productIdToUpdate, Product product)
    {
        Optional<Product> checkIfProductExists = productsRepository.findById(productIdToUpdate);

        if (checkIfProductExists.isPresent()) {
            Product productToUpdate = checkIfProductExists.get();
            productToUpdate.setName(product.getName());
            productToUpdate.setDescription(product.getDescription());
            productToUpdate.setPrice(product.getPrice());
            productToUpdate.setBrand(product.getBrand());
            productToUpdate.setStatus(product.getStatus());

            productsRepository.save(productToUpdate);
            return productToUpdate;
    }
        else
        {
            return new Product();
        }
        
    }

    public void deleteProduct(Long id)
    {
        productsRepository.deleteById(id);
    }

    public boolean productNameExists(String Name)
    {
        return productsRepository.findByName(Name).isPresent();

    }

    public boolean productIdExists(Long id)
    {
        return productsRepository.findById(id).isPresent();
    }
} 

