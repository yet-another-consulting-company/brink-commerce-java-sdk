package com.brinkcommerce.api.stock;

import com.brinkcommerce.api.configuration.BrinkConfiguration;
import com.brinkcommerce.api.product.BrinkProduct;
import com.brinkcommerce.api.exception.BrinkException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BrinkStockApi {

    private static final String CONTENT_TYPE = "application/json";
    public static final String STOCK_URN = "/productv1/stocks";
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final BrinkConfiguration config;
    private final URI stockUri;

    private BrinkStockApi(BrinkConfiguration config) {
        this.config = Objects.requireNonNull(config, "Configuration must be provided");
        this.mapper = Objects.requireNonNull(config.mapper(), "Objectmapper must be provided");
        this.httpClient = Objects.requireNonNull(config.httpClient(), "HttpClient cannot be null");
        this.stockUri = URI.create(this.config.host() + STOCK_URN);
    }

    public static BrinkStockApi create(BrinkConfiguration config) {
        return new BrinkStockApi(config);
    }

    public Optional<BrinkStockUpdate> update(final BrinkProduct product) {
        Objects.requireNonNull(product, "Product can not be null when updating stock level");
        return update(new BrinkStockUpdate(product.id(), product.stock()));
    }

    public Optional<BrinkStockUpdate> update(String productId, Integer stockLevel) {
        return update(new BrinkStockUpdate(productId, stockLevel));
    }

    public List<BrinkStockUpdate> update(final List<BrinkStockUpdate> stockUpdate) {
        return stockUpdate.stream()
                .map(this::updateExceptionHandler)
                .filter(Objects::nonNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<BrinkStockUpdate> updateExceptionHandler(BrinkStockUpdate brinkStockUpdate) {
        try {
            return update(brinkStockUpdate);
        } catch (BrinkException e) {
            return Optional.empty();
        }
    }

    public Optional<BrinkStockUpdate> update(final BrinkStockUpdate stockUpdate) {
        Objects.requireNonNull(stockUpdate, "BrinkStockUpdate can not be null when updating stock level");
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(this.stockUri)
                    .header("Content-Type", CONTENT_TYPE)
                    .headers(BrinkConfiguration.AUTHORIZATION_HEADER, this.config.key())
                    .PUT(HttpRequest.BodyPublishers.ofString(this.mapper.writeValueAsString(stockUpdate)))
                    .build();

            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Optional.ofNullable(this.mapper.readValue(response.body(), BrinkStockUpdate.class));
        } catch (Exception e) {
            throw new BrinkStockException("Could not update stock", e);
        }
    }
}
