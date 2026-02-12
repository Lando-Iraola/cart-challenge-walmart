package com.test_shop.cart.service;

import java.math.BigDecimal;

public class MoneyService {
    private BigDecimal value;
    private double tax;

    public MoneyService(BigDecimal value, double tax){
        this.value = value;
        this.tax = 1+tax;
    }

    public MoneyService(BigDecimal value){
        this.value = value;
    }

    public String getValue(){
        return this.value.toPlainString();
    }

    public String getValueWithTax(){
        return this.value.multiply(new BigDecimal(tax)).toPlainString();
    }

    public BigDecimal add(String value){
        this.value = this.value.add(new BigDecimal(value));
        return this.value;
    }
}
