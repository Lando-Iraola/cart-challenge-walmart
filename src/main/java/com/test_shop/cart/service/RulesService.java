package com.test_shop.cart.service;

import java.util.ArrayList;

import com.test_shop.cart.service.rules.KnownRules;
import com.test_shop.cart.service.rules.targets.BrandList;
import com.test_shop.cart.service.rules.targets.PaymentProcessorsList;
import com.test_shop.cart.service.rules.targets.ProductList;
import com.test_shop.cart.service.rules.targets.RuleTarget;

public class RulesService {
    private String description;
    private byte weight;
    private RuleTarget target;
    private boolean stackWithOtherRules;
    private KnownRules rule;

    public RulesService(String description, byte weight, RuleTarget target, boolean stackWithOtherRules, KnownRules rule){
        this.description = description;
        this.weight = weight;
        this.target = target;
        this.stackWithOtherRules = stackWithOtherRules;
        this.rule = rule;
    }

    public String getDescription(){
        return this.description;
    }

    public byte getWeight(){
        return this.weight;
    }

    public RuleTarget getRuleTarget(){
        return this.target;
    }

    public boolean getStackWithOtherRules(){
        return this.stackWithOtherRules;
    }

    public KnownRules getRule(){
        return this.rule;
    }

    public boolean isInTargetList(BrandService brand){
        switch(this.target){
            case BrandList list -> {
                ArrayList<BrandService> brands = list.items();
                boolean isPresent = brands.contains(brand);
                return isPresent;
            }
            default -> System.out.println("Unknown data type");
        }

        return false;
    }
    public boolean isInTargetList(ProductService product){
        switch(this.target){
            case ProductList list -> {
                ArrayList<ProductService> products = list.items();
                boolean isPresent = products.contains(product);
                return isPresent;
            }
            default -> System.out.println("Unknown data type");
        }

        return false;
    }
    public boolean isInTargetList(PaymentProcessorService payment){
        switch(this.target){
            case PaymentProcessorsList list -> {
                ArrayList<PaymentProcessorService> paymentProcessor = list.items();
                boolean isPresent = paymentProcessor.contains(payment);
                return isPresent;
            }
            default -> System.out.println("Unknown data type");
        }

        return false;
    }
}



