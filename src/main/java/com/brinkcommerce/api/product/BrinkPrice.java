package com.brinkcommerce.api.product;

import com.brinkcommerce.api.store.BrinkCurrencyCode;

public record BrinkPrice(BrinkCurrencyCode currencyUnit, Integer amount) {
    public static BrinkPrice create(BrinkCurrencyCode currency, int amount) {
        return new BrinkPrice(currency, amount);
    }
}
