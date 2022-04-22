package com.brinkcommerce.api.product;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BrinkProductType {

    VARIANT("variant"),
    PRODUCT("product"),
    BUNDLE("bundle");

    private final String displayName;

    @JsonValue
    public String displayName() {
        return this.displayName;
    }

    BrinkProductType(String displayName) {
        this.displayName = displayName;
    }
}
