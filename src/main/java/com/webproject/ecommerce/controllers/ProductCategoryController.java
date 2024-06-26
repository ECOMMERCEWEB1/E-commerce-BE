package com.webproject.ecommerce.controllers;


import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.ProductCategoryDTO;
import com.webproject.ecommerce.entities.ProductCategory;
import com.webproject.ecommerce.mappers.ProductCategoryMapper;
import com.webproject.ecommerce.repositories.ProductCategoryRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.webproject.ecommerce.services.ProductCategoryService;

/**
 * REST controller for managing {@link com.webproject.ecommerce.entities.ProductCategory}.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials= "true")
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryController.class);

    private final ProductCategoryService productCategoryService;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper productCategoryMapper;
    public ProductCategoryController(ProductCategoryService productCategoryService, ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper) {
        this.productCategoryService = productCategoryService;
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    /**
     * {@code POST  /product-categories} : Create a new productCategory.
     *
     * @param productCategory the productCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productCategory, or with status {@code 400 (Bad Request)} if the productCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductCategoryDTO> createProductCategory(@Valid @RequestBody ProductCategory productCategory)
            throws URISyntaxException {
        log.debug("REST request to save ProductCategory : {}", productCategory);
        if (productCategory.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productCategoryMapper.toDto(productCategory, "Product Category already has an Id !"));
        }
        ProductCategory result = productCategoryService.save(productCategory);
        return ResponseEntity
                .created(new URI("/api/product-categories/" + result.getId()))
                .body( productCategoryMapper.toDto(productCategory, "product category created successfully !"));

    }

    /**
     * {@code PUT  /product-categories/:id} : Updates an existing productCategory.
     *
     * @param id the id of the productCategory to save.
     * @param productCategory the productCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategory,
     * or with status {@code 400 (Bad Request)} if the productCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productCategory couldn't be updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> updateProductCategory(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody ProductCategory productCategory
    ){
        log.debug("REST request to update ProductCategory : {}, {}", id, productCategory);
        if(productCategory.getId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productCategoryMapper.toDto(productCategory, "Invalid ID (Null value) !"));
        if (!productCategoryRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productCategoryMapper.toDto(productCategory, "ID does not exist !"));
        if (!Objects.equals(id, productCategory.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productCategoryMapper.toDto(productCategory, "Invalid ID, query parameter ID:"+id+" != product category's Id:"+productCategory.getId()+" !"));

        ProductCategory result = productCategoryService.update(productCategory);
        return ResponseEntity
                .ok()
                .body(productCategoryMapper.toDto(result, "Product Category updated successfully !"));

    }

    /**
     * {@code PATCH  /product-categories/:id} : Partial updates given fields of an existing productCategory, field will ignore if it is null
     *
     * @param id the id of the productCategory to save.
     * @param productCategory the productCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCategory,
     * or with status {@code 400 (Bad Request)} if the productCategory is not valid,
     * or with status {@code 404 (Not Found)} if the productCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the productCategory couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductCategoryDTO> partialUpdateProductCategory(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody ProductCategory productCategory
    ){
        log.debug("REST request to partial update ProductCategory partially : {}, {}", id, productCategory);
        if (productCategory.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productCategoryMapper.toDto(productCategory, "Invalid ID (Null value) !"));
        }
        if (!Objects.equals(id, productCategory.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productCategoryMapper.toDto(productCategory, "Invalid ID, query parameter ID:"+id+" != product category's Id:"+productCategory.getId()+" !"));
        }

        if (!productCategoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productCategoryMapper.toDto(productCategory, "ID does not exist !"));
        }

        Optional<ProductCategory> result = productCategoryService.partialUpdate(productCategory);

        return result.map(category -> ResponseEntity
                        .ok()
                        .body(productCategoryMapper.toDto(category, "Product Category updated successfully !")))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(productCategoryMapper.toDto(productCategory, "Could not update the product category. Internal server error!")));
    }

    /**
     * {@code GET  /product-categories} : get all the productCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productCategories in body.
     */
    @GetMapping("")
    public List<ProductCategory> getAllProductCategories() {
        log.debug("REST request to get all ProductCategories");
        return productCategoryService.findAll();
    }

    /**
     * {@code GET  /product-categories/:id} : get the "id" productCategory.
     *
     * @param id the id of the productCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getProductCategory(@PathVariable("id") Long id) {
        log.debug("REST request to get ProductCategory : {}", id);
        Optional<ProductCategory> productCategory = productCategoryService.findOne(id);
        return ResponseEntity.of(productCategory);
    }

    /**
     * {@code GET  /product-categories/count} : get the number of product.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the count of products, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        log.debug("REST request to get Product Category count.");
        return ResponseEntity.ok().body(productCategoryService.countProductCategories());
    }

    /**
     * {@code DELETE  /product-categories/:id} : delete the "id" productCategory.
     *
     * @param id the id of the productCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 202 (ACCEPTED)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProductCategory : {}", id);
        if(productCategoryService.findOne(id).isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Product Category does not exist"));
        try{
            productCategoryService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new MessageDTO("Product Category Deleted Successfully !"));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(new MessageDTO("Error: "+e.getMessage()));
        }

    }
}

