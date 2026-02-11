package com.test_shop.cart.service;

import java.util.ArrayList;

public record BrandList(ArrayList<BrandService> items) implements RuleTarget {}