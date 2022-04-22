package com.brinkcommerce.api.product;


import com.brinkcommerce.api.configuration.BrinkConfiguration;
import com.brinkcommerce.api.exception.BrinkException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class BrinkPriceApi {

    private static final String PATH = "/productv1/products/";
    public static final String CONTENT_TYPE = "Content-Type";

    private final BrinkConfiguration config;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;
    private final URI productUrl;

    private BrinkPriceApi(final BrinkConfiguration config) {
        this.config = Objects.requireNonNull(config, "Configuration must be provided");
        this.mapper = Objects.requireNonNull(config.mapper(), "ObjectMapper must be provided");
        this.httpClient = Objects.requireNonNull(config.httpClient(), "HttpClient cannot be null");
        this.productUrl = URI.create(this.config.host() + PATH);
    }

    public static BrinkPriceApi create(final BrinkConfiguration config) {
        return new BrinkPriceApi(config);
    }

    public Optional<BrinkProduct> update(final BrinkProduct product) {
        final List<BrinkProduct> updatedProducts = update(List.of(product));
        if(Objects.isNull(updatedProducts) || updatedProducts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(updatedProducts.get(0));
    }

    public List<BrinkProduct> update(final List<BrinkProduct> products) {
        Objects.requireNonNull(products, "Can not update a price of null");
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(this.productUrl)
                    .header(CONTENT_TYPE, BrinkConfiguration.CONTENT_TYPE)
                    .headers(BrinkConfiguration.AUTHORIZATION_HEADER, this.config.key())
                    .PUT(HttpRequest.BodyPublishers.ofString(this.mapper.writeValueAsString(products)))
                    .build();

            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Objects.requireNonNullElse(this.mapper.readValue(response.body(), BrinkProductResponseWrapper.class).products(), new ArrayList<>());
        } catch (Exception e) {
            throw new BrinkException("Could not create product", e);
        }
    }

    public Optional<BrinkProduct> update(final String id, final BrinkPrice price) {
        return update(BrinkProduct.builder()
                .withId(id)
                .withPrices(List.of(price))
                .build());
    }
}
