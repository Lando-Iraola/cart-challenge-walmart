package com.test_shop.cart.service.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DiscountRuleTests {
    @Test
    void testFlatRate(){
        //Este descuento fijo esta pensado para grupo de productos
        DiscountRule appliedToProduct = new DiscountRule();
        double flatRate = 0.05;
        double discount = appliedToProduct.CalculateDiscout(flatRate);

        double itemPrice = 2.0;
        int itemsQuantity = 10;
        double totalPrice = itemsQuantity * itemPrice * discount;
        double expectedTotal = 19.0;
        assertEquals(totalPrice, expectedTotal);
    }
}
