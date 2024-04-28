package com.webproject.ecommerce.controllers;


import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.ProductDTO;
import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.mappers.ProductMapper;
import com.webproject.ecommerce.services.ProductsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductsController {
    private final ProductsService productsService;

    private final ProductMapper productMapper;

    private final Logger log = LoggerFactory.getLogger(ProductsController.class);


    public ProductsController(ProductsService productsService, ProductMapper productMapper) {
        this.productsService = productsService;
        this.productMapper = productMapper;
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */
    @GetMapping("/products")
        public ResponseEntity<List<Product>> getProducts(
                Pageable pageable,
            @RequestParam(name = "eagerload", required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a list of Products");
        Page<Product> page;
        if (eagerload){
            page = productsService.findAllWithEagerRelationships(pageable);
        }
        else {
            page = productsService.findAll(pageable);
        }
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /products/:id} : get the "id" product.
     *
     * @param id the id of the product to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the product, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<Product> product = productsService.findOne(id);
        return ResponseEntity.of(product);
    }

    /**
     * {@code GET  /products/count} : get the number of product.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the count of products, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/count")
    public ResponseEntity<Long> getCount() {
        log.debug("REST request to get Product count.");
        return ResponseEntity.ok().body(productsService.countProducts());
    }

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param product the product to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new product,
     * or with status {@code 400 (Bad Request)} if the product has already an ID
     * or with status {@code 500 (Internal Server Error} if the product (name) already exists.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid Product product) throws URISyntaxException {
        log.debug("REST request to save a Product : {}", product);
        if (product.getId() != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productMapper.toDto(product, "Product already has an Id !"));
        if (productsService.productNameExists(product.getName()))
            return ResponseEntity.internalServerError().body(productMapper.toDto(product, "Product already exists !"));
        Product result = productsService.createProduct(product);
        return ResponseEntity.created(new URI("/api/products/" + result.getId())).body(productMapper.toDto(
                result, "Product Created Successfully !"));
    }

    /**
     * {@code PUT  /products/:id} : Updates (fully) an existing product.
     *
     * @param id the id of the product to save.
     * @param product the product to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated product,
     * or with status {@code 400 (Bad Request)} if the product id is not valid,
     * or with status {@code 404 (Not Found)} if the product could not be found,
     * or with status {@code 500 (Internal Server Error)} if the product couldn't be updated.
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable(value = "id") Long id, @Valid @RequestBody Product product) {
        log.debug("REST request to update Product : {}, {}", id, product);
        if(product.getId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productMapper.toDto(product, "Invalid ID (Null value) !"));
        if (!productsService.productIdExists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productMapper.toDto(product, "ID does not exist !"));
        if (!Objects.equals(id, product.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productMapper.toDto(product, "Invalid ID, query parameter ID:"+id+" != product's Id:"+product.getId()+" !"));

        Product result = productsService.updateProduct(product);
            return ResponseEntity.ok().body(productMapper.toDto(result, "Product updated successfully !"));
    }

    /**
     * {@code PATCH  /products/:id} : Partial updates given fields of an existing product, field will ignore if it is null
     *
     * @param id the id of the product to save.
     * @param product the product to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated product,
     * or with status {@code 400 (Bad Request)} if the product is not valid,
     * or with status {@code 404 (Not Found)} if the product is not found,
     * or with status {@code 500 (Internal Server Error)} if the product couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductDTO> partialUpdateProduct(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody Product product
    ){
        log.debug("REST request to partial update Product partially : {}, {}", id, product);
        if(product.getId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productMapper.toDto(product, "Invalid ID (Null value) !"));
        if (!productsService.productIdExists(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productMapper.toDto(product, "ID does not exist !"));
        else
        {
            Optional<Product> result = productsService.partialUpdate(product);
            return result.map(value -> ResponseEntity.ok().body(productMapper.toDto(value,
                    "Product updated successfully !")))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(productMapper.toDto(product, "Could not update the product. Internal server error!")));
        }
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" product.
     *
     * @param id the id of the product to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)},
     * or with status {@code 404 (Not Found)} if the product couldn't be found.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long id)
    {
        log.debug("REST request to delete Product : {}", id);
        if (productsService.productIdExists(id))
        {
            productsService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageDTO("Product Deleted Successfully !"));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Product does not exist"));
    }

}