package com.test_shop.cart.service.rules.targets;

public sealed interface RuleTarget permits BrandList, ProductList, PaymentProcessorsList {}

