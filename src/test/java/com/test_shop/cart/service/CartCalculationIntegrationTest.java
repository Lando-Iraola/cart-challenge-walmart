package com.test_shop.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test_shop.cart.model.*;
import com.test_shop.cart.model.rules.*;
import com.test_shop.cart.repository.RuleRepository;
import com.test_shop.cart.dto.CalculationResult;

@ExtendWith(MockitoExtension.class)
public class CartCalculationIntegrationTest {

    private CartCalculationService calculationService;

    @Mock
    private RuleRepository ruleRepository;

    @BeforeEach
    void setUp() {
        calculationService = new CartCalculationService(ruleRepository);
    }

    /**
     * Helper to match the Service's expected String formatting (2 decimals)
     * This prevents failures where "3570" != "3570.00"
     */
    private String format(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    @Test
    void testCartDiscount() {
        Brand colun = new Brand("Colun");
        Product leche = new Product("Leche", colun, new Money(new BigDecimal("1500")));
        
        PromoRule promo = new PromoRule(2, 1, 2);
        promo.setWeight((byte) 10);
        promo.getTargetProducts().add(leche);

        when(ruleRepository.findAll()).thenReturn(List.of(promo));

        Cart cart = new Cart("0.19");
        cart.addItem(leche, 3); // 2 pay 1 + 1 pay 1 = 3000 base

        BigDecimal netTotal = new BigDecimal("3000");
        BigDecimal totalEsperado = netTotal.multiply(new BigDecimal("1.19"));
        
        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals(format(totalEsperado), res.getFinalTotal());
        assertEquals(1, res.getDetails().size());
    }

    @Test
    void testCartDiscountProductWithAndWithoutDiscount() {
        Brand colun = new Brand("Colun");
        Product leche = new Product("Leche", colun, new Money(new BigDecimal("1500")));
        Product manjar = new Product("Manjar", colun, new Money(new BigDecimal("2000")));

        PromoRule promo = new PromoRule(2, 1, 2);
        promo.setWeight((byte) 10);
        promo.getTargetProducts().add(leche);

        when(ruleRepository.findAll()).thenReturn(List.of(promo));

        Cart cart = new Cart("0.19");
        cart.addItem(leche, 3); // 3000
        cart.addItem(manjar, 1); // 2000

        BigDecimal netTotal = new BigDecimal("5000");
        BigDecimal totalEsperado = netTotal.multiply(new BigDecimal("1.19"));

        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals(format(totalEsperado), res.getFinalTotal());
    }

    @Test
    void testCartDiscountTwoProduct() {
        Brand colun = new Brand("Colun");
        Product leche = new Product("Leche", colun, new Money(new BigDecimal("1500")));
        Product manjar = new Product("Manjar", colun, new Money(new BigDecimal("2000")));

        PromoRule promo = new PromoRule(2, 1, 2);
        promo.setWeight((byte) 10);
        promo.getTargetProducts().add(leche);
        promo.getTargetProducts().add(manjar);

        when(ruleRepository.findAll()).thenReturn(List.of(promo));

        Cart cart = new Cart("0.19");
        cart.addItem(leche, 3);  // 3000
        cart.addItem(manjar, 2); // 2000

        BigDecimal netTotal = new BigDecimal("5000");
        BigDecimal totalEsperado = netTotal.multiply(new BigDecimal("1.19"));

        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals(format(totalEsperado), res.getFinalTotal());
    }

    @Test
    void testCartDiscountTwoDiscountOnePerProduct() {
        Brand colun = new Brand("Colun");
        Product leche = new Product("Leche", colun, new Money(new BigDecimal("1500")));
        Product manjar = new Product("Manjar", colun, new Money(new BigDecimal("2000")));

        PromoRule promoLeche = new PromoRule(2, 1, 2);
        promoLeche.getTargetProducts().add(leche);

        DiscountRule discountManjar = new DiscountRule(new BigDecimal("0.10"));
        discountManjar.getTargetProducts().add(manjar);

        when(ruleRepository.findAll()).thenReturn(List.of(promoLeche, discountManjar));

        Cart cart = new Cart("0.19");
        cart.addItem(leche, 3); // 3000
        cart.addItem(manjar, 1); // 2000 - 10% = 1800

        BigDecimal netTotal = new BigDecimal("4800");
        BigDecimal totalEsperado = netTotal.multiply(new BigDecimal("1.19"));

        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals(format(totalEsperado), res.getFinalTotal());
    }

    @Test
    void testCartDiscountTwoCompitingDiscountOneProduct() {
        Brand colun = new Brand("Colun");
        Product leche = new Product("Leche", colun, new Money(new BigDecimal("1500")));

        PromoRule betterRule = new PromoRule(2, 1, 2);
        betterRule.setWeight((byte) 20);
        betterRule.getTargetProducts().add(leche);

        DiscountRule worseRule = new DiscountRule(new BigDecimal("0.10"));
        worseRule.setWeight((byte) 10);
        worseRule.getTargetProducts().add(leche);

        when(ruleRepository.findAll()).thenReturn(List.of(betterRule, worseRule));

        Cart cart = new Cart("0.19");
        cart.addItem(leche, 3); // Should pick betterRule (3000)

        BigDecimal netTotal = new BigDecimal("3000");
        BigDecimal totalEsperado = netTotal.multiply(new BigDecimal("1.19"));

        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals(format(totalEsperado), res.getFinalTotal());
    }

    @Test
    void testCartDiscountThreeCompitingDiscountOneProductPaymentProcessor() {
        Brand colun = new Brand("Colun");
        Product leche = new Product("Leche", colun, new Money(new BigDecimal("1500")));
        PaymentProcessor masterplop = new PaymentProcessor("masterplop");

        PromoRule rule1 = new PromoRule(2, 1, 2);
        rule1.setWeight((byte) 20);
        rule1.getTargetProducts().add(leche);

        DiscountRule rule2 = new DiscountRule(new BigDecimal("0.10"));
        rule2.setWeight((byte) 10);
        rule2.getTargetProducts().add(leche);

        PaymentProcessorRule rule3 = new PaymentProcessorRule(new BigDecimal("0.60"));
        rule3.setWeight((byte) 40); // HEAVIEST RULE
        rule3.getTargetProcessors().add(masterplop);

        when(ruleRepository.findAll()).thenReturn(List.of(rule1, rule2, rule3));

        Cart cart = new Cart("0.19");
        cart.setPaymentProcessor(masterplop);
        cart.addItem(leche, 3); // 4500 * 0.4 multiplier = 1800

        BigDecimal netTotal = new BigDecimal("1800");
        BigDecimal totalEsperado = netTotal.multiply(new BigDecimal("1.19"));

        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals(format(totalEsperado), res.getFinalTotal());
    }

    @Test
    void testCartDiscountThreeCumulativeDiscounts() {
        Brand colun = new Brand("Colun");
        Product leche = new Product("Leche", colun, new Money(new BigDecimal("1500")));

        DiscountRule freeRule = new DiscountRule(new BigDecimal("1.0"));
        freeRule.setStackWithOtherRules(true);
        freeRule.getTargetProducts().add(leche);

        when(ruleRepository.findAll()).thenReturn(List.of(freeRule));

        Cart cart = new Cart("0.19");
        cart.addItem(leche, 3);

        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals("0.00", res.getFinalTotal());
    }

    @Test
    void testCartNoDiscounts() {
        Brand colun = new Brand("Colun");
        Product leche = new Product("Leche", colun, new Money(new BigDecimal("1500")));

        when(ruleRepository.findAll()).thenReturn(new ArrayList<>());

        Cart cart = new Cart("0.19");
        cart.addItem(leche, 3);

        BigDecimal subtotal = new BigDecimal("4500");
        BigDecimal totalEsperado = subtotal.multiply(new BigDecimal("1.19"));

        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals(format(totalEsperado), res.getFinalTotal());
    }

    @Test
    void testCartNoItems() {
        when(ruleRepository.findAll()).thenReturn(new ArrayList<>());
        Cart cart = new Cart("0.19");

        CalculationResult res = calculationService.calcularTotal(cart);
        assertEquals("0.00", res.getFinalTotal());
        assertEquals(BigDecimal.ZERO.setScale(2), res.getSubtotal().setScale(2));
    }
}