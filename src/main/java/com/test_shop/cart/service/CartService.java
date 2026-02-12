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
            RulesService heavyRule = null;
            for (RulesService descuento : descuentos) {
                if (descuento.isInTargetList(producto.getProduct())
                        || descuento.isInTargetList(producto.getProduct().getBrand()) ||
                        descuento.isInTargetList(paymentProcessorService)) {
                    if (heavyRule == null) {
                        heavyRule = descuento;
                    } else {

                        heavyRule = heavyRule.moreImpolrtantRule(descuento);
                    }
                }
                activeDiscount = ruleDiscount(descuento, producto, activeDiscount, heavyRule);
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

    private double ruleDiscount(RulesService descuento, CartItemService producto, double activeDiscount,
            RulesService heavyRule) {

        boolean isProductTarget = descuento.isInTargetList(producto.getProduct());
        boolean isBrandTarget = descuento.isInTargetList(producto.getProduct().getBrand());
        boolean isPaymentTarget = descuento.isInTargetList(paymentProcessorService);
        if (isProductTarget || isBrandTarget || isPaymentTarget) {
            KnownRules rule = descuento.getRule();
            double discount = switch (rule) {
                case DiscountRule d -> d.CalculateDiscout(descuento.getFlatRateDiscount());
                case PromoRule p -> p.CalculateDiscout(descuento.getQuantityThreshold(),
                        descuento.getDiscountMagnitude(), producto.getQuantity(),
                        descuento.getMaxApplicability());
                case CardIssuerRule c -> c.CalculateDiscout(descuento.getFlatRateDiscount());
                default -> 0.0;
            };

            RulesService applyThisRule = heavyRule.moreImpolrtantRule(descuento);
            boolean playsAlong = descuento.getStackWithOtherRules() && applyThisRule.getStackWithOtherRules();

            if (!playsAlong && activeDiscount == 0.0) {
                rule = applyThisRule.getRule();
                discount = switch (rule) {
                    case DiscountRule d -> d.CalculateDiscout(applyThisRule.getFlatRateDiscount());
                    case PromoRule p -> p.CalculateDiscout(applyThisRule.getQuantityThreshold(),
                            applyThisRule.getDiscountMagnitude(), producto.getQuantity(),
                            applyThisRule.getMaxApplicability());
                    case CardIssuerRule c -> c.CalculateDiscout(applyThisRule.getFlatRateDiscount());
                    default -> 0.0;
                };
                activeDiscount += discount;
            } else if (playsAlong && activeDiscount + discount < 1.0) {
                activeDiscount += discount;
            }
        }

        return activeDiscount;
    }
}
