package com.test_shop.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.test_shop.cart.service.rules.CardIssuerRule;
import com.test_shop.cart.service.rules.DiscountRule;
import com.test_shop.cart.service.rules.KnownRules;
import com.test_shop.cart.service.rules.PromoRule;

public class CartService {
    private ArrayList<CartItemService> productos;
    private ArrayList<RulesService> descuentos;
    private MoneyService total;
    private String impuesto;
    private PaymentProcessorService paymentProcessorService;

    public CartService(ArrayList<CartItemService> productos, ArrayList<RulesService> descuentos, String impuesto) {
        this.productos = productos;
        this.descuentos = descuentos;
        this.impuesto = impuesto;
    }

    public CartService(ArrayList<CartItemService> productos, String impuesto) {
        this.productos = productos;
        this.impuesto = impuesto;
    }

    public String CalcularTotal() {
        this.total = new MoneyService(new BigDecimal(0), new BigDecimal(impuesto));
        if (descuentos == null) {
            for (CartItemService producto : productos) {
                BigDecimal price = new BigDecimal(producto.getProduct().getPriceAsString());
                price = price.multiply(new BigDecimal(producto.getQuantity()));
                this.total.add(price.toPlainString());
            }

            return this.total.getValueWithTax();
        }

        for (CartItemService producto : productos) {
            double activeDiscount = 0;
            for (RulesService descuento : descuentos) {
                activeDiscount = ruleDiscount(descuento, producto, activeDiscount);
            }

            if (activeDiscount == 0) {
                BigDecimal price = new BigDecimal(producto.getProduct().getPriceAsString());
                price = price.multiply(new BigDecimal(producto.getQuantity()));
                this.total.add(price.toPlainString());
                continue;
            }

            BigDecimal price = new BigDecimal(producto.getProduct().getPriceAsString());
            price = price.multiply(new BigDecimal(producto.getQuantity()));
            price = price.multiply(new BigDecimal(activeDiscount));
            this.total.add(price.toPlainString());
        }

        return this.total.getValueWithTax();
    }

    private double ruleDiscount(RulesService descuento, CartItemService producto, double activeDiscount) {
        KnownRules rule = descuento.getRule();
        boolean isProductTarget = descuento.isInTargetList(producto.getProduct());
        if (isProductTarget) {
            double discount = switch (rule) {
                case DiscountRule d -> d.CalculateDiscout(descuento.getFlatRateDiscount());
                case PromoRule p -> p.CalculateDiscout(descuento.getQuantityThreshold(),
                        descuento.getDiscountMagnitude(), producto.getQuantity(),
                        descuento.getMaxApplicability());
                default -> 0.0;
            };

            if (!descuento.getStackWithOtherRules() && activeDiscount == 0.0) {
                activeDiscount += discount;
            } else if (descuento.getStackWithOtherRules() && activeDiscount + discount < 1.0) {
                activeDiscount += discount;
            }
        }

        boolean isBrandTarget = descuento.isInTargetList(producto.getProduct().getBrand());
        if (isBrandTarget) {
            double discount = switch (rule) {
                case DiscountRule d -> d.CalculateDiscout(descuento.getFlatRateDiscount());
                case PromoRule p -> p.CalculateDiscout(descuento.getQuantityThreshold(),
                        descuento.getDiscountMagnitude(), producto.getQuantity(),
                        descuento.getMaxApplicability());
                default -> 0.0;
            };

            if (!descuento.getStackWithOtherRules() && activeDiscount == 0.0) {
                activeDiscount += discount;
            } else if (descuento.getStackWithOtherRules() && activeDiscount + discount < 1.0) {
                activeDiscount += discount;
            }

        }
        boolean isPaymentTarget = descuento.isInTargetList(paymentProcessorService);
        if (isPaymentTarget) {
            double discount = switch (rule) {
                case CardIssuerRule c -> c.CalculateDiscout(descuento.getFlatRateDiscount());
                default -> 0.0;
            };
            if (!descuento.getStackWithOtherRules() && activeDiscount == 0.0) {
                activeDiscount += discount;
            } else if (descuento.getStackWithOtherRules() && activeDiscount + discount < 1.0) {
                activeDiscount += discount;
            }
        }

        return activeDiscount;
    }
}
