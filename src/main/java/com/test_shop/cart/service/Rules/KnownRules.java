package com.test_shop.cart.service.Rules;

public sealed interface KnownRules permits DiscountRule, PromoRule, CardIssuerRule {
    default public Double CalculateDiscout(double flatRate){
        return 1.0;
    };
    default public Double CalculateDiscout(int quantityThreshold, int discountMagnitude, int quantity, int discountAppliesThisManyTimes) {return CalculateDiscout();}
}
