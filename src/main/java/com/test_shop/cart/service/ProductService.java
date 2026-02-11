package com.test_shop.cart.service;

import java.math.BigDecimal;

public class ProductService {
    private String name;
    private BrandService brand;
    private MoneyService price;

    public ProductService(String name, BrandService brand, MoneyService price){
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public String getBrand(){
        return this.brand.getName();
    }

    public BigDecimal getPrice(){
        return this.price.getValue();
    }
}
