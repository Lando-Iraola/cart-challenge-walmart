package com.test_shop.cart.service.rules;

public final class PromoRule implements KnownRules {
    @Override
    public double CalculateDiscout(int quantityThreshold, int discountMagnitude, int quantity,
            int discountAppliesThisManyTimes) {
        if (discountAppliesThisManyTimes == 0) {
            throw new IllegalArgumentException("El descuento no puede ser aplicado 0 veces");
        }

        if (quantity < quantityThreshold) {
            return 1.0;
        }

        int itemsBeingPaidFor = (quantity % quantityThreshold) + discountMagnitude;
        if (quantity % quantityThreshold == 0 && quantity > quantityThreshold) {
            int discountOccurences = quantity / quantityThreshold;
            if (discountAppliesThisManyTimes < discountOccurences) {
                itemsBeingPaidFor = (discountMagnitude * discountAppliesThisManyTimes)
                        + (quantity - (quantityThreshold * discountAppliesThisManyTimes));
            } else {
                itemsBeingPaidFor = discountMagnitude * (quantity / quantityThreshold);
            }
        }
        Double discount = (double) itemsBeingPaidFor / quantity;

        return discount;
    }
}
