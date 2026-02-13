package com.test_shop.cart.api;

import com.test_shop.cart.model.Brand;
import com.test_shop.cart.repository.BrandRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/brands")
public class BrandApiController {

    private final BrandRepository brandRepository;

    public BrandApiController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    /**
     * GET /api/brands
     * Returns a JSON array of all registered brands.
     */
    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    /**
     * GET /api/brands/{id}
     * Returns a single brand by its UUID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable UUID id) {
        return brandRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/brands
     * Creates a new brand. Instead of a redirect, it returns the created object 
     * or a 409 Conflict if the name is already taken.
     */
    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody Brand brand) {
        if (brandRepository.existsByName(brand.getName())) {
            // In REST, we use HTTP Status codes instead of Flash Attributes
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Brand name '" + brand.getName() + "' is already registered.");
        }
        
        Brand savedBrand = brandRepository.save(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
    }

    /**
     * DELETE /api/brands/{id}
     * Deletes a brand from the database.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) {
        if (!brandRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        brandRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}