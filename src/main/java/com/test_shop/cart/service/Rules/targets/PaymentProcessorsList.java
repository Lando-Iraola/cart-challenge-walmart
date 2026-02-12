package com.test_shop.cart.service.rules.targets;

import java.util.ArrayList;

import com.test_shop.cart.service.PaymentProcessorService;

public record PaymentProcessorsList(ArrayList<PaymentProcessorService> items) implements RuleTarget {
}
