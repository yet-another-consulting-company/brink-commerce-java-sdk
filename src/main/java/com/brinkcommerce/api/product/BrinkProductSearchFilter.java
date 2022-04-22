package com.brinkcommerce.api.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class BrinkProductSearchFilter {

    private String id;
    private String category;
    private BrinkProductType type;

    private BrinkProductSearchFilter() {

    }

    public static BrinkProductSearchFilter create() {
        return new BrinkProductSearchFilter();
    }


    public List<BrinkProduct> find() {
        return new ArrayList<BrinkProduct>();
    }

    public Stream<BrinkProduct> stream() {
        return new ArrayList<BrinkProduct>().stream();
    }

    public BrinkProduct findFirst() {
        return BrinkProduct.builder()
                .withId("829655_10")
                .withName("Panda Dress Beige")
                .withActive(true)
                .withArchived(false)
                .withType(BrinkProductType.PRODUCT)
                .withCategory("Dresses")
                .build();

    }

    public BrinkProductSearchFilter byId(String... ids) {
        return this.byId(Arrays.asList(ids));
    }

    public BrinkProductSearchFilter byId(List<String> ids) {
        return this;
    }

    public BrinkProductSearchFilter byCategory(String... categories) {
        return this.byCategory(Arrays.asList(categories));
    }

    private BrinkProductSearchFilter byCategory(List<String> categories) {
        return this;
    }

    public Stream<BrinkProduct> findVariants() {


        return null;
    }


    public String getHttpRequest() {
        return "";
    }

    public BrinkProductSearchFilter byType(BrinkProductType productType) {
        this.type = productType;
        return this;
    }
}
