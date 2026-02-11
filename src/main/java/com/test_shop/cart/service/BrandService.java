package com.test_shop.cart.service;


public class BrandService {
    private String name;

    public BrandService(String n){
        this.name = n;
    }

    public String getName(){
        return this.name;
    }
}
