package com.test_shop.cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class Money {

    @Column(name = "price_value", precision = 19, scale = 4)
    private BigDecimal value;

    @Column(name = "price_tax", precision = 19, scale = 4)
    private BigDecimal tax;

    protected Money() {
    }

    public Money(BigDecimal value, BigDecimal tax) {
        this.value = value;
        this.tax = tax.add(BigDecimal.ONE);
    }

    public Money(BigDecimal value) {
        this.value = value;
    }

    public String getValue() {
        return this.value != null ? this.value.toPlainString() : "0.00";
    }

    public String getValueWithTax() {
        if (this.value == null || this.tax == null)
            return "0.00";
        return this.value.multiply(tax)
                .setScale(0, RoundingMode.HALF_UP)
                .toPlainString();
    }

    public BigDecimal add(String valueToAdd) {
        this.value = this.value.add(new BigDecimal(valueToAdd));
        return this.value;
    }
}