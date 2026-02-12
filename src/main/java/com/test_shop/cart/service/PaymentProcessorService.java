package com.test_shop.cart.service;

import java.util.Objects;

public class PaymentProcessorService {
    private String name;
    public PaymentProcessorService(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentProcessorService that)) return false;
        return Objects.equals(name, that.name);
    }
}
