package com.test_shop.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public class CalculationResult {
    private final String finalTotal;
    private final BigDecimal subtotal;
    private final BigDecimal taxAmount;
    private final List<AppliedRuleDetail> details;

    /**
     * AppliedRuleDetail represents the specific impact of a single rule.
     * We use a Record for conciseness and immutability.
     */
    public record AppliedRuleDetail(String description, BigDecimal discountAmount) {}

    public CalculationResult(String finalTotal, BigDecimal subtotal, BigDecimal taxAmount, List<AppliedRuleDetail> details) {
        this.finalTotal = finalTotal;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.details = details;
    }

    // Getters
    public String getFinalTotal() {
        return finalTotal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public List<AppliedRuleDetail> getDetails() {
        return details;
    }
    
    /**
     * Helper method to calculate total savings across all rules.
     */
    public BigDecimal getTotalSavings() {
        return details.stream()
                .map(AppliedRuleDetail::discountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}