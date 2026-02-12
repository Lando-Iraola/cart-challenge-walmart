package com.test_shop.cart.service;

import com.test_shop.cart.model.*;
import com.test_shop.cart.model.rules.*;
import com.test_shop.cart.repository.RuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartCalculationService {

    private final RuleRepository ruleRepository;

    public CartCalculationService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Transactional(readOnly = true)
    public String calcularTotal(Cart cart) {
        List<RuleEntity> allRules = ruleRepository.findAll();
        Money total = new Money(BigDecimal.ZERO, new BigDecimal(cart.getTaxRate()));

        for (CartItem item : cart.getItems()) {
            List<RuleEntity> eligibleRules = allRules.stream()
                    .filter(rule -> rule.isEligible(item.getProduct(), cart.getPaymentProcessor()))
                    .collect(Collectors.toList());

            double activeDiscountMultiplier = calculateItemDiscount(item, eligibleRules);

            // FIX: Wrap the String value in a new BigDecimal constructor
            BigDecimal itemBasePrice = item.getProduct().getPrice().getValue();

            BigDecimal lineTotal = itemBasePrice
                    .multiply(new BigDecimal(item.getQuantity()))
                    .multiply(BigDecimal.valueOf(activeDiscountMultiplier));

            total.add(lineTotal.toPlainString());
        }

        return total.getValueWithTax();
    }

    private double calculateItemDiscount(CartItem item, List<RuleEntity> eligibleRules) {
        if (eligibleRules.isEmpty())
            return 1.0;

        // 2. Identify the "Winner": Find the rule with the highest weight for THIS item
        RuleEntity heavyRule = null;
        for (RuleEntity rule : eligibleRules) {
            heavyRule = (heavyRule == null) ? rule : heavyRule.moreImportantRule(rule);
        }

        double multiplier = 1.0;

        for (RuleEntity rule : eligibleRules) {
            // Calculate the specific effect (multiplier) of the current rule
            double ruleEffect = rule.calculateDiscountFactor(item.getQuantity());

            // 3. Supercession Logic:
            // If the current rule OR the heavy rule does NOT allow stacking,
            // we ignore the accumulation and return ONLY the heavy rule's effect.
            if (!rule.isStackWithOtherRules() || !heavyRule.isStackWithOtherRules()) {
                return heavyRule.calculateDiscountFactor(item.getQuantity());
            }

            // 4. Stacking Logic:
            // (1.0 - ruleEffect) calculates the actual discount percentage (e.g., 1.0 - 0.9
            // = 0.1)
            // We subtract that from our current multiplier.
            multiplier -= (1.0 - ruleEffect);
        }

        // 5. Floor Check: Ensures we handle "free items" without going into negative
        // prices.
        return Math.max(0.0, multiplier);
    }
}