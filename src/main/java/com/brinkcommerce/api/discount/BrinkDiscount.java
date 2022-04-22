package com.brinkcommerce.api.discount;

import com.brinkcommerce.api.exception.BrinkException;
import com.brinkcommerce.api.product.BrinkProduct;
import com.brinkcommerce.api.store.BrinkStore;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public record BrinkDiscount(String description, Boolean freeShipping, List<String> excludedCategories,
                            List<BrinkStore> stores, Integer minimumCartItems, Integer usageLimitPerCart, LocalDate start,
                            Integer discountPercentage, List<String> includedCategories,
                            List<String> includedProducts, List<String> excludedProducts, LocalDate end,
                            BrinkDiscountType type, String code) {

    public BrinkDiscount {
        Objects.requireNonNull(start, "A start date must be provided for a discount");
        Objects.requireNonNull(end, "An end date must be provided for a discount");
    }

    public static BrinkDiscountBuilder builder() {
        return BrinkDiscountBuilder.create();
    }

    public static class BrinkDiscountBuilder {

        private final static ZoneId UTC = ZoneId.of("Z");

        private Boolean freeShipping;
        private List<String> excludedCategories;
        private String description;
        private List<BrinkStore> stores;
        private Integer minimumCartItems;
        private Integer usageLimitPerCart;
        private LocalDate start;
        private Integer discountPercentage;
        private List<String> includedCategories;
        private LocalDate end;
        private BrinkDiscountType discountType;
        private String code;
        private List<String> includedProducts;
        private List<String> excludedProducts;

        public BrinkDiscountBuilder() {
            this.freeShipping = false;
        }

        public static BrinkDiscountBuilder create() {
            return new BrinkDiscountBuilder();
        }

        public BrinkDiscount build() {

            return new BrinkDiscount(description,
                    freeShipping,
                    excludedCategories,
                    stores,
                    minimumCartItems,
                    usageLimitPerCart,
                    start,
                    discountPercentage,
                    includedCategories,
                    includedProducts,
                    excludedProducts,
                    end,
                    discountType,
                    code);
        }

        public BrinkDiscountBuilder withFreeShipping(Boolean freeShipping) {
            this.freeShipping = freeShipping;
            return this;
        }

        public BrinkDiscountBuilder withFreeShipping() {
            return this.withFreeShipping(true);
        }

        public BrinkDiscountBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public BrinkDiscountBuilder withExcludedCategories(List<String> excludedCategories) {
            this.excludedCategories = excludedCategories;
            return this;
        }

        public BrinkDiscountBuilder withStores(List<BrinkStore> stores) {
            this.stores = stores;
            return this;
        }

        public BrinkDiscountBuilder withMinCartItems(Integer minimumCartItems) {
            this.minimumCartItems = minimumCartItems;
            return this;
        }

        public BrinkDiscountBuilder withUsageLimitPerCart(Integer usageLimitPerCart) {
            this.usageLimitPerCart = usageLimitPerCart;
            return this;
        }

        public BrinkDiscountBuilder withStart(final Instant start) {
            this.start = LocalDate.ofInstant(start, UTC);
            return this;
        }

        public BrinkDiscountBuilder withEnd(final Instant end) {
            this.end = LocalDate.ofInstant(end, UTC);
            return this;
        }

        public BrinkDiscountBuilder withDiscountPercentage(final Integer discountPercentage) {
            this.discountPercentage = validatePercentage(discountPercentage);
            return this;
        }

        private Integer validatePercentage(final Integer percentageAsInteger) {
            if(percentageAsInteger >= 0 && percentageAsInteger <= 100) {
                return percentageAsInteger;
            }
            throw new BrinkException("Percentage needs to be a value between 0 and 100");
        }

        public BrinkDiscountBuilder withIncludedProducts(final List<String> includedProducts) {
            this.includedProducts = includedProducts;
            return this;
        }

        public BrinkDiscountBuilder withExcludedProducts(final List<String> excludedProducts) {
            this.excludedProducts = excludedProducts;
            return this;
        }

        public BrinkDiscountBuilder withIncludedCategories(final List<String> includedCategories) {
            this.includedCategories = includedCategories;
            return this;
        }

        public BrinkDiscountBuilder withType(BrinkDiscountType discountType) {
            this.discountType = discountType;
            return this;
        }

        public BrinkDiscountBuilder withCode(String code) {
            this.code = code;
            return this;
        }
    }
}
