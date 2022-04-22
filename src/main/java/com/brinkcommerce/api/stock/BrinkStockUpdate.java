package com.brinkcommerce.api.stock;

import java.util.Objects;

public record BrinkStockUpdate(String productId, Integer stock) {

    public BrinkStockUpdate {
        Objects.requireNonNull(productId, "A productId must be provided when updating stock");
        Objects.requireNonNull(stock, "A stock level must be provided when updating stock");
        if(stock < 0) {
            throw new IllegalArgumentException("Stock level can not be negative.");
        }
    }

    public static BrinkStockUpdate create(String id, Integer newStockLevel) {
        return new BrinkStockUpdate(id, newStockLevel);
    }
}
