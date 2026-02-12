package com.test_shop.cart.repository;

import com.test_shop.cart.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

/**
 * Repository interface for Brand entity.
 * JpaRepository provides standard CRUD operations:
 * - save(Brand)
 * - findById(UUID)
 * - findAll()
 * - delete(Brand)
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {

    /**
     * Custom query method to find a brand by its name.
     * Spring Data JPA parses the method name and generates the SQL automatically:
     * SELECT * FROM brands WHERE name = ?
     */
    Optional<Brand> findByName(String name);
    boolean existsByName(String name);
}