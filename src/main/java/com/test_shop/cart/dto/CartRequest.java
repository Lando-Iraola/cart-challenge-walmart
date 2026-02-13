package com.test_shop.cart.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartRequest {
    private List<ItemRequest> items = new ArrayList<>();
    private UUID processorId;

    // Initialize with one empty item so the form shows one row by default
    public CartRequest() {
        this.items.add(new ItemRequest());
    }

    public List<ItemRequest> getItems() { return items; }
    public void setItems(List<ItemRequest> items) { this.items = items; }

    public UUID getProcessorId() { return processorId; }
    public void setProcessorId(UUID processorId) { this.processorId = processorId; }

    public static class ItemRequest {
        private UUID productId;
        private int quantity = 1;

        public UUID getProductId() { return productId; }
        public void setProductId(UUID productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}