package com.test_shop.cart.service.Rules;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PromoRuleTests {
    @Test
    void testDosPorUno(){
        PromoRule promo = new PromoRule();

        //Probando un 2 por 1
        //Este metodo solo devuelve el factor por el cual multiplicar
        //En este caso:
        //Deben haber 2 items para activar el descuento
        //Se hace descuento por 1 item
        //Se ingresaron 2 items para hacer el descuento
        //El descuento aplica maximo 1 vez
        int itemsQuantity = 2;
        
        double discount = promo.CalculateDiscout(2, 1, itemsQuantity, 1);
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discount;
        double expectedTotal = 2.0;
        assertEquals(totalPrice, expectedTotal);
    }

    @Test
    void testDocePorTres(){
        PromoRule promo = new PromoRule();
        int itemsQuantity = 60;
        int threshold = 12;
        int discountMagnitude = 3;
        int applyThiscountUpTo = 3;
        
        double discount = promo.CalculateDiscout(threshold, discountMagnitude, itemsQuantity, applyThiscountUpTo);
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discount;
        double quantityOfItemsBeingPaidFor = 33;
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        assertEquals(totalPrice, expectedTotal);
    }

    @Test
    void testDocePorTresMenosDescuento(){
        PromoRule promo = new PromoRule();
        int itemsQuantity = 60;
        int threshold = 12;
        int discountMagnitude = 3;
        int applyThiscountUpTo = 1;
        
        double discount = promo.CalculateDiscout(threshold, discountMagnitude, itemsQuantity, applyThiscountUpTo);
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discount;
        double quantityOfItemsBeingPaidFor = 51;
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        assertEquals(totalPrice, expectedTotal);
    }

    @Test
    void docientosMilPorUno(){
        PromoRule promo = new PromoRule();
        int itemsQuantity = 400_000;
        int threshold = 200_000;
        int discountMagnitude = 1;
        int applyThiscountUpTo = 2;
        
        double discount = promo.CalculateDiscout(threshold, discountMagnitude, itemsQuantity, applyThiscountUpTo);
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discount;
        double quantityOfItemsBeingPaidFor = 2;
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        assertEquals(totalPrice, expectedTotal);
    }

    @Test
    void docientosMilPorUnoMenosDescuento(){
        PromoRule promo = new PromoRule();
        int itemsQuantity = 400_000;
        int threshold = 200_000;
        int discountMagnitude = 1;
        int applyThiscountUpTo = 1;
        
        double discount = promo.CalculateDiscout(threshold, discountMagnitude, itemsQuantity, applyThiscountUpTo);
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discount;
        double quantityOfItemsBeingPaidFor = 200_001;
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        assertEquals(totalPrice, expectedTotal);
    }

    @Test
    void thresholdNotReached(){
        PromoRule promo = new PromoRule();
        int itemsQuantity = 4;
        int threshold = 6;
        int discountMagnitude = 5;
        int applyThiscountUpTo = 1;
        
        double discount = promo.CalculateDiscout(threshold, discountMagnitude, itemsQuantity, applyThiscountUpTo);
        double itemPrice = 2.0;
        double totalPrice = itemsQuantity * itemPrice * discount;
        double quantityOfItemsBeingPaidFor = 4;
        double expectedTotal = quantityOfItemsBeingPaidFor * itemPrice;
        assertEquals(totalPrice, expectedTotal);
    }
}
