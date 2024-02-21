package com.webproject.ecommerce.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.ProductDTO;
import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.services.ProductsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/products")
        public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok().body(productsService.getProducts());
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid Product product) throws URISyntaxException {
        if (productsService.productNameExists(product.getName()))
            return ResponseEntity.internalServerError().body(new ProductDTO(product, "Product already exists !"));
        else
            productsService.createProduct(product);
        return ResponseEntity.created(new URI("/api/products")).body(new ProductDTO(product, "product created successfully !"));       
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable(value = "id") Long id, @Valid @RequestBody Product product) {
        if (!productsService.productIdExists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductDTO(product, "ID does not exist !"));
        else
        {
            productsService.updateProduct(id, product);
            return ResponseEntity.ok().body(new ProductDTO(product, "Product updated successfully !"));
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long id)
    {
        if (!productsService.productIdExists(id))
        {
            productsService.deleteProduct(id);
            return ResponseEntity.ok().body(new MessageDTO("Product Deleted Successfully !"));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Product does not exist"));
    }

}