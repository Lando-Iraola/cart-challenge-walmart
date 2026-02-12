package com.test_shop.cart.repository;

import com.test_shop.cart.model.PaymentProcessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

/**
 * Repository for PaymentProcessor entity.
 * Used to retrieve processors to check against RuleEntity targets.
 */
@Repository
public interface PaymentProcessorRepository extends JpaRepository<PaymentProcessor, UUID> {

    /**
     * Finds a processor by its name (e.g., "VISA").
     * Essential for the DataInitializer or for identifying the 
     * processor used in a Cart.
     */
    Optional<PaymentProcessor> findByName(String name);
}