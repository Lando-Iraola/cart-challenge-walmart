package com.test_shop.cart.service.rules.targets;

import java.util.ArrayList;

import com.test_shop.cart.service.BrandService;

public record BrandList(ArrayList<BrandService> items) implements RuleTarget {}