package com.test_shop.cart.model.rules;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("FLAT_DISCOUNT")
public final class DiscountRule extends RuleEntity {

    @Column(name = "flat_rate_discount", precision = 19, scale = 4)
    private BigDecimal flatRateDiscount;

    // Standard constructor for JPA
    public DiscountRule() {
        super();
    }

    // Constructor for manual setup
    public DiscountRule(BigDecimal flatRateDiscount) {
        super();
        this.flatRateDiscount = flatRateDiscount;
    }

    /**
     * Logic for a flat percentage discount.
     * Uses the helper method from the KnownRules interface.
     */
    @Override
    public double calculateDiscountFactor(int quantity) {
        if (this.flatRateDiscount == null) {
            return 1.0;
        }
        // Converts BigDecimal to double and calls interface helper
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