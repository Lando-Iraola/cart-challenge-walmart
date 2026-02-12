package com.test_shop.cart.service;

import com.test_shop.cart.model.*;
import com.test_shop.cart.model.rules.*;
import com.test_shop.cart.repository.RuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
            double activeDiscountMultiplier = calculateItemDiscount(item, cart.getPaymentProcessor(), allRules);
            
            BigDecimal itemBasePrice = new BigDecimal(item.getProduct().getPrice().getValue());
            BigDecimal lineTotal = itemBasePrice
                    .multiply(new BigDecimal(item.getQuantity()))
                    .multiply(BigDecimal.valueOf(activeDiscountMultiplier));

            total.add(lineTotal.toPlainString());
        }

        return total.getValueWithTax();
    }

    private double calculateItemDiscount(CartItem item, PaymentProcessor processor, List<RuleEntity> rules) {
        double multiplier = 1.0;
        RuleEntity heavyRule = null;

        for (RuleEntity rule : rules) {
            if (rule.isEligible(item.getProduct(), processor)) {
                heavyRule = (heavyRule == null) ? rule : heavyRule.moreImportantRule(rule);
            }
        }

        if (heavyRule == null) return 1.0;

        for (RuleEntity rule : rules) {
            if (rule.isEligible(item.getProduct(), processor)) {
                // CHANGED: calling calculateDiscountFactor instead of calculateDiscount
                double ruleEffect = rule.calculateDiscountFactor(item.getQuantity());

                if (!rule.isStackWithOtherRules() || !heavyRule.isStackWithOtherRules()) {
                    // CHANGED: heavyRule also uses calculateDiscountFactor
                    return heavyRule.calculateDiscountFactor(item.getQuantity());
                }

                multiplier -= (1.0 - ruleEffect);
            }
        }

        return Math.max(0.0, multiplier);
    }
}