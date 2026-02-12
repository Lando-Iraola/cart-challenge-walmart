package com.test_shop.cart.controller;

import com.test_shop.cart.model.Brand;
import com.test_shop.cart.model.PaymentProcessor;
import com.test_shop.cart.model.Product;
import com.test_shop.cart.model.rules.DiscountRule;
import com.test_shop.cart.model.rules.PromoRule;
import com.test_shop.cart.model.rules.RuleEntity;
import com.test_shop.cart.repository.BrandRepository;
import com.test_shop.cart.repository.PaymentProcessorRepository;
import com.test_shop.cart.repository.ProductRepository;
import com.test_shop.cart.repository.RuleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/rules")
public class RuleController {

    private final RuleRepository ruleRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final PaymentProcessorRepository processorRepository;

    public RuleController(RuleRepository ruleRepository, 
                          ProductRepository productRepository,
                          BrandRepository brandRepository, 
                          PaymentProcessorRepository processorRepository) {
        this.ruleRepository = ruleRepository;
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.processorRepository = processorRepository;
    }

    @GetMapping("/new")
    public String showRuleForm(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("processors", processorRepository.findAll());
        model.addAttribute("rules", ruleRepository.findAll());
        return "rule-form";
    }

    @PostMapping("/save")
    public String saveRule(
            @RequestParam String ruleType,
            @RequestParam String description,
            @RequestParam byte weight,
            @RequestParam(defaultValue = "false") boolean stackable,
            // Target Selection (All Optional)
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) UUID brandId,
            @RequestParam(required = false) UUID processorId,
            // Promo Specific Fields
            @RequestParam(required = false) Integer threshold,
            @RequestParam(required = false) Integer magnitude,
            @RequestParam(required = false) Integer maxApps,
            // Discount Specific Fields
            @RequestParam(required = false) BigDecimal flatRate) {

        RuleEntity rule;

        // 1. Determine Behavioral Type
        if ("PROMO".equals(ruleType)) {
            PromoRule p = new PromoRule();
            p.setQuantityThreshold(threshold != null ? threshold : 0);
            p.setDiscountMagnitude(magnitude != null ? magnitude : 0);
            p.setMaxApplicability(maxApps != null ? maxApps : 0);
            rule = p;
        } else {
            DiscountRule d = new DiscountRule();
            d.setFlatRateDiscount(flatRate != null ? flatRate : BigDecimal.ZERO);
            rule = d;
        }

        // 2. Set Abstract Parent Fields
        rule.setDescription(description);
        rule.setWeight(weight);
        rule.setStackWithOtherRules(stackable);

        // 3. Populate Many-to-Many Target Lists
        // This satisfies RuleEntity.isEligible() logic
        if (productId != null) {
            productRepository.findById(productId).ifPresent(rule.getTargetProducts()::add);
        }
        if (brandId != null) {
            brandRepository.findById(brandId).ifPresent(rule.getTargetBrands()::add);
        }
        if (processorId != null) {
            processorRepository.findById(processorId).ifPresent(rule.getTargetProcessors()::add);
        }

        ruleRepository.save(rule);
        return "redirect:/rules/new?success";
    }

    @PostMapping("/delete/{id}")
    public String deleteRule(@PathVariable UUID id) {
        ruleRepository.deleteById(id);
        return "redirect:/rules/new";
    }
}