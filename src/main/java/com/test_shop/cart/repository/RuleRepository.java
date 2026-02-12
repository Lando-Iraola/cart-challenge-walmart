package com.test_shop.cart.repository;

import com.test_shop.cart.model.rules.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

/**
 * Polymorphic repository for RuleEntity.
 * Because RuleEntity uses InheritanceType.SINGLE_TABLE, this repository
 * will return a mixed list of PromoRule, DiscountRule, and PaymentProcessorRule.
 */
@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, UUID> {

    /**
     * Finds rules based on their stacking preference.
     * Helpful if you want to apply non-stackable rules first in your service.
     */
    List<RuleEntity> findByStackWithOtherRulesTrue();

    /**
     * Standard find all, but sorted by weight.
     * This ensures the CartCalculationService processes higher priority 
     * rules in the correct order.
     */
    List<RuleEntity> findAllByOrderByWeightDesc();
}