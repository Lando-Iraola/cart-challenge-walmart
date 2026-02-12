package com.test_shop.cart.repository;

import com.test_shop.cart.model.Product;
import com.test_shop.cart.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

/**
 * Repository for Product entity.
 * This handles the retrieval of products and their associations with Brands.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Finds all products belonging to a specific brand.
     * Useful for applying brand-wide discounts.
     */
    List<Product> findByBrand(Brand brand);

    /**
     * Finds products by name (partial match, case-insensitive).
     * Useful for search features in the shop.
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}