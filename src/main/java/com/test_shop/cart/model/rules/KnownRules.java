package com.test_shop.cart.model.rules;

/**
 * The Contract for all pricing logic.
 * Default implementations provide a safety net so rules 
 * only need to override the math they actually change.
 */
public interface KnownRules {

    /**
     * Helper for percentage-based discounts.
     * Converts a rate (e.g., 0.15) into a multiplier (0.85).
     * * @param flatRate The discount percentage as a decimal (0.0 to 1.0)
     * @return The multiplier to apply to the base price
     */
    default double getFlatDiscountFactor(double flatRate) {
        if (flatRate > 1.0) {
            // Safety check: prevents discounts from accidentally increasing the price
            throw new IllegalArgumentException("Descuento inválido, se intentó aumentar el precio");
        }

        return 1.0 - flatRate;
    }

    /**
     * The primary calculation method for the Rule Engine.
     * Defaults to 1.0 (no discount) if not overridden.
     * * @param quantity The number of units of the product in the cart
     * @return The final multiplier for the line item
     */
    default double calculateDiscountFactor(int quantity) {
        return 1.0;
    }
}