package com.brinkcommerce.api;

import com.brinkcommerce.api.configuration.BrinkConfiguration;
import com.brinkcommerce.api.discount.BrinkDiscountApi;
import com.brinkcommerce.api.product.BrinkPriceApi;
import com.brinkcommerce.api.product.BrinkProductApi;
import com.brinkcommerce.api.stock.BrinkStockApi;
import com.brinkcommerce.api.store.BrinkStoreApi;

public class BrinkApi {

    private final BrinkStockApi stockApi;
    private final BrinkProductApi productApi;
    private final BrinkPriceApi priceApi;
    private final BrinkStoreApi storeApi;
    private final BrinkDiscountApi discountApi;

    private BrinkApi(BrinkConfiguration config) {
        this.productApi = BrinkProductApi.create(config);
        this.stockApi = BrinkStockApi.create(config);
        this.priceApi = BrinkPriceApi.create(config);
        this.storeApi = BrinkStoreApi.create(config);
        this.discountApi = BrinkDiscountApi.create(config);
    }

    public static BrinkApi create(BrinkConfiguration config) {
        return new BrinkApi(config);
    }

    public BrinkProductApi product() {
        return this.productApi;
    }

    public BrinkStockApi stock() {
        return this.stockApi;
    }

    public BrinkPriceApi price() {
        return this.priceApi;
    }

    public BrinkStoreApi store() {
        return this.storeApi;
    }

    public BrinkDiscountApi discount() { return this.discountApi;}

    public BrinkStoreApi store(Integer maxBulkSize) {
        return this.storeApi.maxBulkSize(maxBulkSize);
    }
}
