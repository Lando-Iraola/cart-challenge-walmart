package com.test_shop.cart.service;

import org.junit.jupiter.api.Test;

import com.test_shop.cart.service.rules.KnownRules;
import com.test_shop.cart.service.rules.PromoRule;
import com.test_shop.cart.service.rules.targets.ProductList;
import com.test_shop.cart.service.rules.targets.RuleTarget;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RulesServiceTests {
    @Test
    void testRuleServiceFindsProduct(){

        String description = "A test ruleservice";
        byte weight = 10;
        BrandService colun = new BrandService("Colun");
        ProductService a = new ProductService("Leche", colun, new MoneyService(new BigDecimal("1500")));
        RuleTarget target = new ProductList(new ArrayList<>(List.of(a)));
        boolean stackWithOtherRules = false;
        KnownRules rule = new PromoRule();

        RulesService rs = new RulesService(description, weight, target, stackWithOtherRules, rule);

        ProductService b = new ProductService("Leche", colun, new MoneyService(new BigDecimal("1500")));
 
        assertTrue(rs.isInTargetList(b));
    }

    @Test
    void testRuleServiceNotFindsProduct(){

        String description = "A test ruleservice";
        byte weight = 10;
        BrandService colun = new BrandService("Colun");
        ProductService a = new ProductService("Leche", colun, new MoneyService(new BigDecimal("1500")));
        RuleTarget target = new ProductList(new ArrayList<>(List.of(a)));
        boolean stackWithOtherRules = false;
        KnownRules rule = new PromoRule();

        RulesService rs = new RulesService(description, weight, target, stackWithOtherRules, rule);

        ProductService b = new ProductService("Arroz", new BrandService("Miraflores"), new MoneyService(new BigDecimal("1500")));
 
        assertFalse(rs.isInTargetList(b));
    }
}
