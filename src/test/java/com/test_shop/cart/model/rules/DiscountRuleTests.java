package com.test_shop.cart.model.rules;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountRuleTests {

    @Test
    void testFlatRate() {
        // We initialize the rule with a 5% discount (0.05)
        BigDecimal flatRate = new BigDecimal("0.05");
        DiscountRule appliedToProduct = new DiscountRule(flatRate);

        double itemPrice = 2.0;
        int itemsQuantity = 10;
        
        // The entity now knows its own rate, so we only pass the quantity
        double discountFactor = appliedToProduct.calculateDiscountFactor(itemsQuantity);

        double totalPrice = itemsQuantity * itemPrice * discountFactor;
        
        // Math: 10 * 2.0 = 20.0. 
        // 20.0 * 0.95 (5% off) = 19.0.
        double expectedTotal = 19.0;
        
        assertEquals(expectedTotal, totalPrice, 0.001);
    }
}