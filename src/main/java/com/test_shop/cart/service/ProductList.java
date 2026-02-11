package com.test_shop.cart.service;
import java.util.ArrayList;

public record ProductList(ArrayList<ProductService> items) implements RuleTarget {}