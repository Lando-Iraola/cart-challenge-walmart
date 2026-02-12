package com.test_shop.cart.dto;

import java.util.UUID;

public class CartRequest {
    private UUID productId;
    private UUID processorId;

    // Getters and Setters
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public UUID getProcessorId() { return processorId; }
    public void setProcessorId(UUID processorId) { this.processorId = processorId; }
}