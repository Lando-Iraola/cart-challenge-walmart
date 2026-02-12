package com.test_shop.cart.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyService {
    private BigDecimal value;
    private BigDecimal tax;

    public MoneyService(BigDecimal value, BigDecimal tax){
        this.value = value;
        this.tax = tax.add(new BigDecimal(1));
    }

    public MoneyService(BigDecimal value){
        this.value = value;
    }

    public String getValue(){
        return this.value.toPlainString();
    }

    public String getValueWithTax(){
        return this.value.multiply(tax).setScale(0, RoundingMode.HALF_UP).toPlainString();
    }

    public BigDecimal add(String value){
        this.value = this.value.add(new BigDecimal(value));
        return this.value;
    }
}
