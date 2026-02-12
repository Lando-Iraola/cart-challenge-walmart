package com.test_shop.cart.model.rules;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PROMO")
public final class PromoRule extends RuleEntity {

    private int quantityThreshold;
    private int discountMagnitude;
    private int maxApplicability;

    // Standard constructor for JPA
    public PromoRule() {
        super();
    }

    // Constructor for manual instantiation (Tests/DataInitializer)
    public PromoRule(int quantityThreshold, int discountMagnitude, int maxApplicability) {
        super();
        this.quantityThreshold = quantityThreshold;
        this.discountMagnitude = discountMagnitude;
        this.maxApplicability = maxApplicability;
    }

    /**
     * Logic for "Buy X Pay for Y".
     * Example: Buy 3 Pay for 2. Factor = 0.66.
     */
    @Override
    public double calculateDiscountFactor(int quantity) {
        if (this.maxApplicability <= 0 || quantity < this.quantityThreshold) {
            return 1.0;
        }

        int possibleApplications = quantity / quantityThreshold;
        int actualApplications = Math.min(possibleApplications, this.maxApplicability);

        // Savings = (What you would have paid) - (What you actually pay)
        int itemsSavedPerApplication = quantityThreshold - discountMagnitude;
        int totalItemsSaved = actualApplications * itemsSavedPerApplication;

        double paidQuantity = (double) quantity - totalItemsSaved;
        
        return paidQuantity / quantity;
    }

    // --- Manual Getters and Setters ---

    public int getQuantityThreshold() { return quantityThreshold; }
    public void setQuantityThreshold(int quantityThreshold) { this.quantityThreshold = quantityThreshold; }

    public int getDiscountMagnitude() { return discountMagnitude; }
    public void setDiscountMagnitude(int discountMagnitude) { this.discountMagnitude = discountMagnitude; }

    public int getMaxApplicability() { return maxApplicability; }
    public void setMaxApplicability(int maxApplicability) { this.maxApplicability = maxApplicability; }
}