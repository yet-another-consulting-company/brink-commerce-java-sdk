package com.brinkcommerce.api.discount;

import com.brinkcommerce.api.configuration.BrinkConfiguration;
import com.brinkcommerce.api.exception.BrinkException;
import com.brinkcommerce.api.product.BrinkProductResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class BrinkDiscountApi {

    private static final String DISCOUNT_URN = "/productv1/discounts";
    private static final String CONTENT_TYPE = "Content-Type";
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final BrinkConfiguration config;
    private final URI discountUri;

    private BrinkDiscountApi(final BrinkConfiguration config) {
        this.config = Objects.requireNonNull(config, "Configuration must be provided");
        this.mapper = Objects.requireNonNull(config.mapper(), "ObjectMapper must be provided");
        this.httpClient = Objects.requireNonNull(config.httpClient(), "HttpClient cannot be null");
        this.discountUri = URI.create(this.config.host() + DISCOUNT_URN);
    }

    public Optional<BrinkDiscount> create(final BrinkDiscount discount) {
        Objects.requireNonNull(discount, "Can not create a discount of null");
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(this.discountUri)
                    .header(CONTENT_TYPE, BrinkConfiguration.CONTENT_TYPE)
                    .headers(BrinkConfiguration.AUTHORIZATION_HEADER, this.config.key())
                    .PUT(HttpRequest.BodyPublishers.ofString(this.mapper.writeValueAsString(discount)))
                    .build();

            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Optional.ofNullable(this.mapper.readValue(response.body(), BrinkDiscount.class));
        } catch (Exception e) {
            throw new BrinkDiscountException("Could not create product", e);
        }
    }

    public static BrinkDiscountApi create(BrinkConfiguration config) {
        return new BrinkDiscountApi(config);
    }

    public Optional<BrinkDiscount> update(final BrinkDiscount discount) {
        return Optional.empty();
    }


    //TODO: Implement discount api
}
