package com.test_shop.cart.service;

import com.test_shop.cart.model.*;
import com.test_shop.cart.model.rules.*;
import com.test_shop.cart.dto.CalculationResult;
import com.test_shop.cart.repository.RuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CartCalculationService {

    private final RuleRepository ruleRepository;

    public CartCalculationService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Transactional(readOnly = true)
    public CalculationResult calcularTotal(Cart cart) {
        List<RuleEntity> allRules = ruleRepository.findAll();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscounts = BigDecimal.ZERO;
        List<CalculationResult.AppliedRuleDetail> ruleDetails = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            BigDecimal itemPrice = item.getProduct().getPrice().getValue();
            BigDecimal lineBase = itemPrice.multiply(new BigDecimal(item.getQuantity()));
            subtotal = subtotal.add(lineBase);

            List<RuleEntity> eligible = allRules.stream()
                .filter(rule -> rule.isEligible(item.getProduct(), cart.getPaymentProcessor()))
                .collect(Collectors.toList());

            
            // 2. We now calculate WHICH rules contributed to that multiplier for the UI
            // To be accurate to your behavior, we check if stacking was allowed
            RuleEntity heavyRule = findHeavyRule(eligible);
            
            if (heavyRule != null) {
                if (!heavyRule.isStackWithOtherRules()) {
                    // Scenario A: Non-stackable "Winner" takes all
                    BigDecimal discount = calculateSavings(lineBase, heavyRule, item.getQuantity());
                    ruleDetails.add(new CalculationResult.AppliedRuleDetail(heavyRule.getDescription(), discount));
                    totalDiscounts = totalDiscounts.add(discount);
                } else {
                    // Scenario B: Stacking logic
                    for (RuleEntity rule : eligible) {
                        if (rule.isStackWithOtherRules()) {
                            BigDecimal discount = calculateSavings(lineBase, rule, item.getQuantity());
                            ruleDetails.add(new CalculationResult.AppliedRuleDetail(rule.getDescription(), discount));
                            totalDiscounts = totalDiscounts.add(discount);
                        }
                    }
                }
            }
        }

        BigDecimal netTotal = subtotal.subtract(totalDiscounts).max(BigDecimal.ZERO);
        BigDecimal taxAmount = netTotal.multiply(new BigDecimal(cart.getTaxRate()));
        
        return new CalculationResult(
            netTotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP).toPlainString(),
            subtotal,
            taxAmount,
            ruleDetails
        );
    }

    private RuleEntity findHeavyRule(List<RuleEntity> rules) {
        RuleEntity heavy = null;
        for (RuleEntity r : rules) {
            heavy = (heavy == null) ? r : heavy.moreImportantRule(r);
        }
        return heavy;
    }

    private BigDecimal calculateSavings(BigDecimal lineBase, RuleEntity rule, int qty) {
        double factor = rule.calculateDiscountFactor(qty);
        return lineBase.multiply(BigDecimal.valueOf(1.0 - factor));
    }
}