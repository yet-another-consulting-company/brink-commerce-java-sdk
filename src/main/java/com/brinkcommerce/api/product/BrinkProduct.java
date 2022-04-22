package com.brinkcommerce.api.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record BrinkProduct(String id, String name, BrinkProductType type, String category, Boolean active, Boolean archived,
                           Integer stock, String imageUrl, String url, Instant created, Instant lastUpdate,
                           List<BrinkPrice> price) {
    //TODO: Price in long

    public BrinkProduct {
        Objects.requireNonNull(id, "ID must be set when creating a product");
    }

    public static BrinkProductBuilder builder() {
        return BrinkProductBuilder.create();
    }

    @JsonIgnore
    public Boolean isVariant() {
        return isVariant(this);
    }

    public static boolean isVariant(final BrinkProduct p) {
        return Objects.equals(p.type, BrinkProductType.VARIANT);
    }

    public static boolean isProduct(BrinkProduct p) {
        return Objects.equals(p.type, BrinkProductType.PRODUCT);
    }
}
