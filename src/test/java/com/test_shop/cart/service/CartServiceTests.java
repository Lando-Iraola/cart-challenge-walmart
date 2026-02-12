package com.test_shop.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.test_shop.cart.service.rules.DiscountRule;
import com.test_shop.cart.service.rules.KnownRules;
import com.test_shop.cart.service.rules.PromoRule;
import com.test_shop.cart.service.rules.targets.ProductList;
import com.test_shop.cart.service.rules.targets.RuleTarget;

public class CartServiceTests {
    @Test
    void testCartDiscount() {

        String description = "A test ruleservice";
        byte weight = 10;
        BrandService colun = new BrandService("Colun");
        ProductService leche = new ProductService("Leche", colun, new MoneyService(new BigDecimal("1500")));
        RuleTarget target = new ProductList(new ArrayList<>(List.of(leche)));
        boolean stackWithOtherRules = false;
        KnownRules rule = new PromoRule();
        byte maxApplicability = 2;
        int quantityThreshold = 2;
        int discountMagnitude = 1;
        double flatRateDiscount = 0.0;

        RulesService rs = new RulesService(description, weight, target, stackWithOtherRules, rule, maxApplicability,
                quantityThreshold, discountMagnitude, flatRateDiscount);

        CartItemService cis = new CartItemService(leche, 3);

        CartService cs = new CartService(new ArrayList<>(List.of(cis)), new ArrayList<>(List.of(rs)), "0.19");
        
        BigDecimal totalEsperado = new BigDecimal("3000");
        totalEsperado = totalEsperado.multiply(new BigDecimal("1.19")).setScale(0, RoundingMode.HALF_UP);
        String res = cs.CalcularTotal();
        assertEquals(res, totalEsperado.toPlainString());
    }

    @Test
    void testCartDiscountProductWithAndWithoutDiscount() {

        String description = "A test ruleservice";
        byte weight = 10;
        BrandService colun = new BrandService("Colun");
        ProductService leche = new ProductService("Leche", colun, new MoneyService(new BigDecimal("1500")));
        
        RuleTarget target = new ProductList(new ArrayList<>(List.of(leche)));
        boolean stackWithOtherRules = false;
        KnownRules rule = new PromoRule();
        byte maxApplicability = 2;
        int quantityThreshold = 2;
        int discountMagnitude = 1;
        double flatRateDiscount = 0.0;

        RulesService rs = new RulesService(description, weight, target, stackWithOtherRules, rule, maxApplicability,
                quantityThreshold, discountMagnitude, flatRateDiscount);

        CartItemService cis = new CartItemService(leche, 3);
        ProductService manjar = new ProductService("Manjar", colun, new MoneyService(new BigDecimal("2000")));
        CartItemService cis2 = new CartItemService(manjar, 1);

        CartService cs = new CartService(new ArrayList<>(List.of(cis, cis2)), new ArrayList<>(List.of(rs)), "0.19");
        
        BigDecimal totalEsperado = new BigDecimal("5000");
        totalEsperado = totalEsperado.multiply(new BigDecimal("1.19")).setScale(0, RoundingMode.HALF_UP);
        String res = cs.CalcularTotal();
        assertEquals(res, totalEsperado.toPlainString());
    }
    @Test
    void testCartDiscountTwoProduct() {

        String description = "A test ruleservice";
        byte weight = 10;
        BrandService colun = new BrandService("Colun");
        ProductService leche = new ProductService("Leche", colun, new MoneyService(new BigDecimal("1500")));
        ProductService manjar = new ProductService("Manjar", colun, new MoneyService(new BigDecimal("2000")));
        RuleTarget target = new ProductList(new ArrayList<>(List.of(leche, manjar)));
        boolean stackWithOtherRules = false;
        KnownRules rule = new PromoRule();
        byte maxApplicability = 2;
        int quantityThreshold = 2;
        int discountMagnitude = 1;
        double flatRateDiscount = 0.0;

        RulesService rs = new RulesService(description, weight, target, stackWithOtherRules, rule, maxApplicability,
                quantityThreshold, discountMagnitude, flatRateDiscount);

        CartItemService cis = new CartItemService(leche, 3);
        
        CartItemService cis2 = new CartItemService(manjar, 2);

        CartService cs = new CartService(new ArrayList<>(List.of(cis, cis2)), new ArrayList<>(List.of(rs)), "0.19");
        
        BigDecimal totalEsperado = new BigDecimal("5000");
        totalEsperado = totalEsperado.multiply(new BigDecimal("1.19")).setScale(0, RoundingMode.HALF_UP);
        String res = cs.CalcularTotal();
        assertEquals(res, totalEsperado.toPlainString());
    }

    @Test
    void testCartDiscountTwoDiscountOnePerProduct() {

        String description = "A test ruleservice";
        byte weight = 10;
        BrandService colun = new BrandService("Colun");
        ProductService leche = new ProductService("Leche", colun, new MoneyService(new BigDecimal("1500")));
        ProductService manjar = new ProductService("Manjar", colun, new MoneyService(new BigDecimal("2000")));
        RuleTarget target = new ProductList(new ArrayList<>(List.of(leche)));
        RuleTarget target2 = new ProductList(new ArrayList<>(List.of(manjar)));
        boolean stackWithOtherRules = false;
        KnownRules rule = new PromoRule();
        byte maxApplicability = 2;
        int quantityThreshold = 2;
        int discountMagnitude = 1;
        double flatRateDiscount = 0.0;

        RulesService rs = new RulesService(description, weight, target, stackWithOtherRules, rule, maxApplicability,
                quantityThreshold, discountMagnitude, flatRateDiscount);
        KnownRules rule2 = new DiscountRule();
        RulesService rs2 = new RulesService(description, weight, target2, stackWithOtherRules, rule2, maxApplicability,
                0, 0, 0.1);

        CartItemService cis = new CartItemService(leche, 3);
        
        CartItemService cis2 = new CartItemService(manjar, 1);

        CartService cs = new CartService(new ArrayList<>(List.of(cis, cis2)), new ArrayList<>(List.of(rs, rs2)), "0.19");
        
        BigDecimal totalEsperado = new BigDecimal("4800");
        totalEsperado = totalEsperado.multiply(new BigDecimal("1.19")).setScale(0, RoundingMode.HALF_UP);
        String res = cs.CalcularTotal();
        assertEquals(res, totalEsperado.toPlainString());
    }
}
