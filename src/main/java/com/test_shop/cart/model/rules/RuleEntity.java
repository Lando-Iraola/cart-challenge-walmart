package com.test_shop.cart.model.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.test_shop.cart.model.Brand;
import com.test_shop.cart.model.PaymentProcessor;
import com.test_shop.cart.model.Product;

import jakarta.persistence.*;

@Entity
@Table(name = "reglas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rule_type", discriminatorType = DiscriminatorType.STRING)
/* * Removed 'sealed' and 'permits' to resolve IncompatibleClassChangeError.
 * Hibernate 6 can now safely create proxies for this class.
 */
public abstract class RuleEntity implements KnownRules {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private byte weight;
    private String description;
    private boolean stackWithOtherRules;

    @ManyToMany
    @JoinTable(name = "rule_products")
    private List<Product> targetProducts = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "rule_brands")
    private List<Brand> targetBrands = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "rule_processors")
    private List<PaymentProcessor> targetProcessors = new ArrayList<>();

    // Standard constructor for JPA
    protected RuleEntity() {}

    // --- Logic Methods ---

    public boolean isEligible(Product product, PaymentProcessor processor) {
        if (product != null) {
            if (targetProducts.contains(product)) return true;
            if (product.getBrand() != null && targetBrands.contains(product.getBrand())) return true;
        }
        if (processor != null && targetProcessors.contains(processor)) return true;
        return false;
    }

    public RuleEntity moreImportantRule(RuleEntity other) {
        if (other == null) return this;
        return (other.getWeight() > this.weight) ? other : this;
    }

    // --- Manual Getters and Setters ---

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public byte getWeight() { return weight; }
    public void setWeight(byte weight) { this.weight = weight; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isStackWithOtherRules() { return stackWithOtherRules; }
    public void setStackWithOtherRules(boolean stackWithOtherRules) { this.stackWithOtherRules = stackWithOtherRules; }

    public List<Product> getTargetProducts() { return targetProducts; }
    public List<Brand> getTargetBrands() { return targetBrands; }
    public List<PaymentProcessor> getTargetProcessors() { return targetProcessors; }
}