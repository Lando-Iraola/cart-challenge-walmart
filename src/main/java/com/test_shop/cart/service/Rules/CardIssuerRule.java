package com.test_shop.cart.service.Rules;

public final class CardIssuerRule implements KnownRules {
    @Override
    public Double CalculateDiscout(double flatRate){
        return flatRate;
    }
}
