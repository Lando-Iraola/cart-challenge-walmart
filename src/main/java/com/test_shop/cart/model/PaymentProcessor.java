package com.test_shop.cart.model;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "payment_processors")
public class PaymentProcessor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    // Standard constructor for JPA
    public PaymentProcessor() {
    }

    // Constructor for manual setup
    public PaymentProcessor(String name) {
        this.name = name;
    }

    // --- Manual Getters and Setters ---

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // --- Overriding Equals and HashCode ---
    // Critical for RuleEntity.isEligible(product, processor)
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PaymentProcessor that))
            return false;
        if (id == null || that.id == null)
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}