package com.test_shop.cart.service;

public class PaymentProcessorService {
    private String name;
    public PaymentProcessorService(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
