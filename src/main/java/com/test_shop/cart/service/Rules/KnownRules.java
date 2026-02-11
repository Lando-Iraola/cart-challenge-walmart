package com.test_shop.cart.service.Rules;

public sealed interface KnownRules permits DiscountRule, PromoRule, CardIssuerRule {
    public Double CalculateDiscout();
}
