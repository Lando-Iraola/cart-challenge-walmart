package com.test_shop.cart.service;

import java.util.Objects;

public class BrandService {
    private String name;

    public BrandService(String n){
        this.name = n;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BrandService that)) return false;
        return Objects.equals(name, that.name);
    }
}
