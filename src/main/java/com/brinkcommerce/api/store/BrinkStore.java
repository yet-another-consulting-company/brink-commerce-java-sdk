package com.brinkcommerce.api.store;

import java.time.Instant;

public record BrinkStore(BrinkCountryCode countryCode, BrinkLanguageCode languageCode, BrinkCurrencyCode currencyUnit,
                         Integer tax, Instant created, Instant lastUpdated) {

    public static BrinkStoreBuilder builder() {
        return BrinkStoreBuilder.create();
    }
}
