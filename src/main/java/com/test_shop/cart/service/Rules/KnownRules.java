package com.test_shop.cart.service.rules;

public sealed interface KnownRules permits DiscountRule, PromoRule, CardIssuerRule {
    default public double CalculateDiscout(double flatRate){
        if (flatRate > 1.0) {
            throw new IllegalArgumentException("Descuento invalido, se intento aumentar el precio");
        }
        return 1.0 - flatRate;
    };
    default public double CalculateDiscout(int quantityThreshold, int discountMagnitude, int quantity, int discountAppliesThisManyTimes) {return 1.0;}
}
