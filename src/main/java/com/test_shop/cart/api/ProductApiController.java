package com.test_shop.cart.api;

import com.test_shop.cart.model.Product;
import com.test_shop.cart.repository.ProductRepository;
import com.test_shop.cart.repository.BrandRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public ProductApiController(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    /**
     * GET /api/products
     * Returns all products with their associated brands and prices.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/products
     * Expects JSON: { "name": "Leche", "brand": {"id": "..."}, "price": {"value": 1500} }
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        // Basic Validation: Ensure the brand exists before saving
        if (product.getBrand() == null || product.getBrand().getId() == null) {
            return ResponseEntity.badRequest().body("A valid Brand ID is required.");
        }

        if (!brandRepository.existsById(product.getBrand().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brand not found.");
        }

        try {
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            // Catches the unique constraint violation (UK_PRODUCT_NAME_BRAND)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A product with this name already exists for this brand.");
        }
    }

    /**
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}