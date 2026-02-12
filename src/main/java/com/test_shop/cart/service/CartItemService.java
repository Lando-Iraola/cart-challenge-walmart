package com.test_shop.cart.service;

public class CartItemService {
    private ProductService product;
    private int quantity = 1;

    public CartItemService(ProductService product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    public CartItemService(ProductService product){
        this.product = product;
    }

    public int addQuantity(int quantity){
        this.quantity += quantity;
        return this.quantity;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public ProductService getProduct(){
        return this.product;
    }

}
