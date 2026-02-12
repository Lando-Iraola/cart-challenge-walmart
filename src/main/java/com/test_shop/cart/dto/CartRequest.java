package com.test_shop.cart.dto;

import java.util.UUID;

public class CartRequest {
    private UUID productId;
    private UUID processorId;
    private int quantity = 1; // Default to 1

    // Getters and Setters
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public UUID getProcessorId() { return processorId; }
    public void setProcessorId(UUID processorId) { this.processorId = processorId; }
}