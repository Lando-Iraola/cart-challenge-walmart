package com.test_shop.cart.model.rules;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("PAYMENT_RULE")
public final class PaymentProcessorRule extends RuleEntity {

    @Column(name = "processor_discount_rate", precision = 19, scale = 4)
    private BigDecimal flatRateDiscount;

    // Standard constructor for JPA
    protected PaymentProcessorRule() {
        super();
    }

    // Constructor for manual setup
    public PaymentProcessorRule(BigDecimal flatRateDiscount) {
        super();
        this.flatRateDiscount = flatRateDiscount;
    }

    /**
     * Logic for payment-specific discounts.
     */
    @Override
    public double calculateDiscountFactor(int quantity) {
        if (this.flatRateDiscount == null) {
            return 1.0;
        }
        return getFlatDiscountFactor(this.flatRateDiscount.doubleValue());
    }

    // --- Manual Getters and Setters ---

    public BigDecimal getFlatRateDiscount() {
        return flatRateDiscount;
    }

    public void setFlatRateDiscount(BigDecimal flatRateDiscount) {
        this.flatRateDiscount = flatRateDiscount;
    }
}