package com.test_shop.cart.model;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Many line items can belong to one Cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Each line item points to one specific Product
    @ManyToOne(fetch = FetchType.EAGER) // Usually Eager because you always need the product details
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity = 1;

    // JPA mandatory no-args constructor
    public CartItem() {
    }

    public CartItem(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    // --- REUSED LOGIC FROM YOUR ORIGINAL CARTITEMSERVICE ---

    public int addQuantity(int extraQuantity) {
        this.quantity += extraQuantity;
        return this.quantity;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public Cart getCart() {
        return cart;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CartItem that))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}