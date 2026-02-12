package com.test_shop.cart.service.rules.targets;

import java.util.ArrayList;

import com.test_shop.cart.service.ProductService;

public record ProductList(ArrayList<ProductService> items) implements RuleTarget {
}