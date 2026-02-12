package com.test_shop.cart.model.rules;

import org.junit.jupiter.api.Test;
import com.test_shop.cart.model.*;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RuleEntityEligibilityTests {

    @Test
    void testRuleFindsProductByUuid() {
        // Setup IDs to simulate database-persistent objects
        UUID productId = UUID.randomUUID();
        Brand colun = new Brand("Colun");
        
        // Product A is in the rule
        Product productA = new Product("Leche", colun, new Money(new BigDecimal("1500")));
        productA.setId(productId);

        // We use DiscountRule as a concrete implementation of the abstract RuleEntity
        RuleEntity rule = new DiscountRule(new BigDecimal("0.10"));
        rule.getTargetProducts().add(productA);

        // Product B is a "different" object in memory but has the same ID (Identity)
        Product productB = new Product("Leche", colun, new Money(new BigDecimal("1500")));
        productB.setId(productId);

        assertTrue(rule.isEligible(productB, null), "Rule should recognize product by its UUID");
    }

    @Test
    void testRuleFindsProductByBrand() {
        UUID brandId = UUID.randomUUID();
        Brand brand = new Brand("Colun");
        brand.setId(brandId);

        RuleEntity rule = new DiscountRule(new BigDecimal("0.10"));
        rule.getTargetBrands().add(brand);

        Product product = new Product("Yogurt", brand, new Money(new BigDecimal("500")));
        
        assertTrue(rule.isEligible(product, null), "Rule should apply to any product of the targeted brand");
    }

    @Test
    void testRuleDoesNotFindUnrelatedProduct() {
        Brand brandA = new Brand("Colun");
        Brand brandB = new Brand("Miraflores");
        brandA.setId(UUID.randomUUID());
        brandB.setId(UUID.randomUUID());

        Product productA = new Product("Leche", brandA, new Money(new BigDecimal("1500")));
        productA.setId(UUID.randomUUID());

        RuleEntity rule = new DiscountRule(new BigDecimal("0.10"));
        rule.getTargetProducts().add(productA);

        Product productB = new Product("Arroz", brandB, new Money(new BigDecimal("1000")));
        productB.setId(UUID.randomUUID());

        assertFalse(rule.isEligible(productB, null), "Rule should not apply to an unrelated product");
    }
}