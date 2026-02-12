package com.test_shop.cart.model.rules;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentProcessorRuleTests {

    @Test
    void testFlatRate() {
        // Initialize with a 5% discount (0.05)
        BigDecimal flatRate = new BigDecimal("0.05");
        PaymentProcessorRule cardRule = new PaymentProcessorRule(flatRate);

        double itemPrice = 2.0;
        int itemsQuantity = 10;
        
        // The entity uses its internal flatRateDiscount field
        double discountFactor = cardRule.calculateDiscountFactor(itemsQuantity);

        // Calculation: (10 * 2.0) * (1.0 - 0.05) = 19.0
        double totalPrice = itemsQuantity * itemPrice * discountFactor;
        double expectedTotal = 19.0;
        
        assertEquals(expectedTotal, totalPrice, 0.001);
    }
}