package com.test_shop.cart.service;

import java.math.BigDecimal;

public class MoneyService {
    private BigDecimal value;

    public MoneyService(BigDecimal value){
        this.value = value;
    }

    public BigDecimal getValue(){
        return this.value;
    }
}
