package com.test_shop.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.test_shop.cart.model.*;
import com.test_shop.cart.model.rules.*;
import com.test_shop.cart.repository.*;

@SpringBootTest
@Transactional // This rolls back database changes after every test automatically
class CartCalculationIntegrationTest {

    @Autowired private CartCalculationService calculationService;
    @Autowired private ProductRepository productRepository;
    @Autowired private BrandRepository brandRepository;
    @Autowired private RuleRepository ruleRepository;
    @Autowired private PaymentProcessorRepository processorRepository;
    @Autowired private CartRepository cartRepository;

    @Test
    void testComplexRuleStackingWithH2() {
        // 1. Setup Infrastructure in H2
        Brand colun = brandRepository.save(new Brand("Colun"));
        
        Product leche = productRepository.save(
            new Product("Leche", colun, new Money(new BigDecimal("1500")))
        );

        PaymentProcessor masterplop = processorRepository.save(
            new PaymentProcessor("masterplop")
        );

        // 2. Setup Rules in H2
        // Rule A: Promo "Buy 2 Get 1" (Weight 20, Stackable)
        PromoRule promo = new PromoRule(2, 1, (byte) 2);
        promo.setWeight((byte) 20);
        promo.setStackWithOtherRules(true);
        promo.getTargetProducts().add(leche);
        ruleRepository.save(promo);

        // Rule B: Payment Discount 60% (Weight 40, Stackable)
        PaymentProcessorRule payRule = new PaymentProcessorRule(new BigDecimal("0.6"));
        payRule.setWeight((byte) 40);
        payRule.setStackWithOtherRules(true);
        payRule.getTargetProcessors().add(masterplop);
        ruleRepository.save(payRule);

        // 3. Create Cart and Calculate
        Cart cart = new Cart("0.19");
        cart.setPaymentProcessor(masterplop);
        cart.addItem(leche, 3); // 3 units
        cartRepository.save(cart);

        // EXPECTED MATH:
        // Base: 4500
        // Promo Effect: Pay 2, get 1 free -> Discount factor 0.666... (removes 1500)
        // Payment Effect: 60% off -> Discount factor 0.40 (removes 60% of original price)
        // Cumulative logic: multiplier = 1.0 - (1.0 - 0.666) - (1.0 - 0.40) = 0.066...
        // Result: Approx 300 base + 19% tax.
        
        String result = calculationService.calcularTotal(cart);
        
        // Asserting against your specific logic results
        // (Adjust the string value based on your exact rounding preference)
        assertEquals("357", result); 
    }
}