package com.shopsphere.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Cartitem {
    private final Product product;
    private final int quantity;
    private final BigDecimal unitPrice;

    public Cartitem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        BigDecimal discount = BigDecimal.valueOf(Math.max(0, Math.min(product.getDiscount(), 100)));
        this.unitPrice = BigDecimal.valueOf(product.getPrice())
                .multiply(BigDecimal.ONE.subtract(discount.movePointLeft(2)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}