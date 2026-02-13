package com.test_shop.cart.api;

import com.test_shop.cart.model.rules.RuleEntity;
import com.test_shop.cart.repository.RuleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rules")
public class RuleApiController {

    private final RuleRepository ruleRepository;

    public RuleApiController(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    /**
     * GET /api/rules
     * Returns all rules (Promo, Discount, and Payment) sorted by weight.
     */
    @GetMapping
    public List<RuleEntity> getAllRules() {
        return ruleRepository.findAllByOrderByWeightDesc();
    }

    /**
     * POST /api/rules
     * Create any rule type by providing the "type" field in JSON.
     */
    @PostMapping
    public ResponseEntity<RuleEntity> createRule(@RequestBody RuleEntity rule) {
        // Validation: Ensure many-to-many relations exist (optional but recommended)
        // Hibernate will handle simple ID mapping if the nested objects have IDs.
        RuleEntity savedRule = ruleRepository.save(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRule);
    }

    /**
     * DELETE /api/rules/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        if (!ruleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ruleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}