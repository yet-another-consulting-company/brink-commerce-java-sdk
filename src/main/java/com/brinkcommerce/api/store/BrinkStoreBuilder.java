package com.brinkcommerce.api.store;

public class BrinkStoreBuilder {

    private BrinkCountryCode countryCode;
    private BrinkCurrencyCode currency;
    private BrinkLanguageCode languageCode;
    private Integer tax;

    public static BrinkStoreBuilder create() {
        return new BrinkStoreBuilder();
    }

    public BrinkStore build() {
        return new BrinkStore(this.countryCode, this.languageCode, this.currency, this.tax, null, null);
    }

    public BrinkStoreBuilder withCountryCode(BrinkCountryCode countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public BrinkStoreBuilder withCurrency(BrinkCurrencyCode currency) {
        this.currency = currency;
        return this;
    }

    public BrinkStoreBuilder withLanguageCode(BrinkLanguageCode languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public BrinkStoreBuilder withTax(Integer tax) {
        this.tax = tax;
        return this;
    }
}
