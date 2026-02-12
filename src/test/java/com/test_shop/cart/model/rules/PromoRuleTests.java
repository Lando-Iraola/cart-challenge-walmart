package com.test_shop.cart.model.rules;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PromoRuleTests {

    @Test
    void testDosPorUno() {
        // threshold: 2, magnitude: 1, maxApplicability: 1
        PromoRule promo = new PromoRule(2, 1, 1);

        int itemsQuantity = 2;
        double discountFactor = promo.calculateDiscountFactor(itemsQuantity);
        
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discountFactor;
        double expectedTotal = 2.0;
        
        assertEquals(expectedTotal, totalPrice, 0.001);
    }

    @Test
    void testDocePorTres() {
        // threshold: 12, magnitude: 3, maxApplicability: 3
        PromoRule promo = new PromoRule(12, 3, 3);
        
        int itemsQuantity = 60;
        double discountFactor = promo.calculateDiscountFactor(itemsQuantity);
        
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discountFactor;
        
        // logic: 3 applications * 3 free items = 9 free items. 
        // 60 - 9 = 51 items paid.
        // Note: Your original test expected 33 items paid, which implies a different math 
        // interpretation. I have kept your expected value logic below:
        double quantityOfItemsBeingPaidFor = 33; 
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        
        assertEquals(expectedTotal, totalPrice, 0.001);
    }

    @Test
    void testDocePorTresMenosDescuento() {
        // threshold: 12, magnitude: 3, maxApplicability: 1
        PromoRule promo = new PromoRule(12, 3, 1);
        
        int itemsQuantity = 60;
        double discountFactor = promo.calculateDiscountFactor(itemsQuantity);
        
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discountFactor;
        
        double quantityOfItemsBeingPaidFor = 51; // 60 - (3 * 1) = 57? 
        // I will stick to your original expected values to ensure consistency with your previous logic
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        
        assertEquals(expectedTotal, totalPrice, 0.001);
    }

    @Test
    void docientosMilPorUno() {
        // threshold: 200,000, magnitude: 1, maxApplicability: 2
        PromoRule promo = new PromoRule(200_000, 1, 2);
        
        int itemsQuantity = 400_000;
        double discountFactor = promo.calculateDiscountFactor(itemsQuantity);
        
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discountFactor;
        
        // Original expected: 2? That would mean almost all items are free.
        double quantityOfItemsBeingPaidFor = 2; 
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        
        assertEquals(expectedTotal, totalPrice, 0.001);
    }

    @Test
    void docientosMilPorUnoMenosDescuento() {
        PromoRule promo = new PromoRule(200_000, 1, 1);
        
        int itemsQuantity = 400_000;
        double discountFactor = promo.calculateDiscountFactor(itemsQuantity);
        
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discountFactor;
        
        double quantityOfItemsBeingPaidFor = 200_001;
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        
        assertEquals(expectedTotal, totalPrice, 0.001);
    }

    @Test
    void thresholdNotReached() {
        // threshold: 6, magnitude: 5, maxApplicability: 1
        PromoRule promo = new PromoRule(6, 5, 1);
        
        int itemsQuantity = 4;
        double discountFactor = promo.calculateDiscountFactor(itemsQuantity);
        
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discountFactor;
        
        double quantityOfItemsBeingPaidFor = 4;
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        
        assertEquals(expectedTotal, totalPrice, 0.001);
    }
}