package com.brinkcommerce.api.product;

import com.brinkcommerce.api.exception.BrinkException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BrinkProductBuilder {

    private String id;
    private String name;
    private Boolean active;
    private Boolean archived;
    private BrinkProductType type;
    private String category;
    private String parent;
    private String imageUrl;
    private Integer stockLevel;
    private String slug;
    private List<BrinkPrice> prices;

    private BrinkProductBuilder(){}

    public static BrinkProductBuilder create() {
        return new BrinkProductBuilder();
    }

    public BrinkProduct build() {
        if(Objects.isNull(this.parent) && Objects.equals(BrinkProductType.VARIANT, this.type)) {
            throw new BrinkException("No orphans allowed! A variant must have a parent product.");
        }
        //TODO Build the product
        return new BrinkProduct(
                this.id,
                this.name,
                this.type,
                this.category,
                this.active,
                this.archived,
                this.stockLevel,
                this.slug,
                this.imageUrl,
                null,
                null,
                Objects.requireNonNullElse(this.prices, new ArrayList<>()));
    }

    public BrinkProductBuilder withId(String id) {
        this.id = Objects.requireNonNull(id);
        return this;
    }

    public BrinkProductBuilder withPrices(List<BrinkPrice> prices) {
        this.prices = prices;
        return this;
    }

    public BrinkProductBuilder withName(String name) {
        this.name = Objects.requireNonNull(name);
        return this;
    }

    public BrinkProductBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public BrinkProductBuilder withArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public BrinkProductBuilder withType(BrinkProductType type) {
        this.type = type;
        return this;
    }

    public BrinkProductBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public BrinkProductBuilder withNoParent() {
        this.parent = null;
        return this;
    }

    public BrinkProductBuilder withParent(String parentId) {
        this.parent = parentId;
        return this;
    }

    public BrinkProductBuilder withParent(BrinkProduct product) {
        if(Objects.equals(product.type(), BrinkProductType.PRODUCT)) {
            this.type = BrinkProductType.VARIANT;
            this.parent = product.id();
            return this;

        }
        throw new BrinkException("Cannot link a variant to another variant");
    }

    public BrinkProductBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BrinkProductBuilder withStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
        return this;
    }

    public BrinkProductBuilder withSlug(String slug) {
        this.slug = slug;
        return this;
    }
}
