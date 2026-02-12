package com.test_shop.cart.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "carritos")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String taxRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processor_id")
    private PaymentProcessor paymentProcessor;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    // Standard constructor for JPA
    public Cart() {
    }

    // Constructor for manual setup
    public Cart(String taxRate) {
        this.taxRate = taxRate;
    }

    // --- Helper Methods ---

    /**
     * Adds an item to the cart and ensures the CartItem knows
     * which cart it belongs to (Bidirectional synchronization).
     */
    public void addItem(Product product, int quantity) {
        CartItem item = new CartItem(this, product, quantity);
        this.items.add(item);
    }

    // --- Manual Getters and Setters ---

    public void setItems(List<CartItem> item){
        this.items = item;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public PaymentProcessor getPaymentProcessor() {
        return paymentProcessor;
    }

    public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public List<CartItem> getItems() {
        return items;
    }

    // We typically don't provide a setter for a List in JPA to avoid
    // replacing the Hibernate-managed collection instance.

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}