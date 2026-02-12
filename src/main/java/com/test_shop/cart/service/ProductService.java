package com.test_shop.cart.service;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductService that)) return false;
        return Objects.equals(name, that.name) && 
           Objects.equals(brand, that.brand);
    }
}
