package com.test_shop.cart.repository;

import com.test_shop.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Repository for managing shopping carts.
 * Because of CascadeType.ALL on the Cart entity, saving a Cart 
 * through this repository will also save all its CartItems.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    
    // Standard JpaRepository methods handle the basics:
    // cartRepository.save(myCart);
    // cartRepository.findById(someUuid);
}