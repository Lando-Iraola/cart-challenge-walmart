package com.test_shop.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

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
}
