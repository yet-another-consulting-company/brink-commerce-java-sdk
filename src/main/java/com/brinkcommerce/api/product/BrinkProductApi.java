package com.brinkcommerce.api.product;

import com.brinkcommerce.api.configuration.BrinkConfiguration;
import com.brinkcommerce.api.exception.BrinkException;
import com.brinkcommerce.api.store.BrinkStoreException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class BrinkProductApi {

    private static final String CONTENT_TYPE = "application/json";
    private static final String PATH = "/productv1/products/";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final BrinkConfiguration config;
    private final URI productUrl;
    private Integer maxBulkSize;

    private BrinkProductApi(BrinkConfiguration config) {
        this.config = Objects.requireNonNull(config, "Configuration must be provided");
        this.mapper = Objects.requireNonNull(config.mapper(), "Objectmapper must be provided");
        this.httpClient = Objects.requireNonNull(config.httpClient(), "HttpClient cannot be null");
        this.productUrl = URI.create(this.config.host() + PATH);
        this.maxBulkSize = BrinkConfiguration.DEFAULT_MAX_BULK_SIZE;
    }

    public static BrinkProductApi create(BrinkConfiguration config) {
        return new BrinkProductApi(config);
    }

    public Optional<BrinkProduct> create(final BrinkProduct brinkProduct) {
        final List<BrinkProduct> brinkProducts = this.create(List.of(brinkProduct));
        if(brinkProducts.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(brinkProducts.get(0));
        }
    }

    public BrinkProduct getProductById(String id) {
        return BrinkProduct.builder()
                .withId(id)
                .withName("Panda Dress Beige")
                .withActive(true)
                .withArchived(false)
                .withType(BrinkProductType.PRODUCT)
                .withCategory("Dresses")
                .build();
    }

    public Integer stock(BrinkProduct product) {
        return 1;
    }

    public List<BrinkProduct> getProductsById(List<String> ids) {
        return new ArrayList<>();
    }

    public List<BrinkProduct> getProductsById(String... ids) {
        return this.getProductsById(Arrays.asList(ids));
    }



    public List<BrinkProduct> create(final List<BrinkProduct> products)  {
        Objects.requireNonNull(products, "Can not create a product of null");
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(this.productUrl)
                    .header("Content-Type", BrinkConfiguration.CONTENT_TYPE)
                    .headers(BrinkConfiguration.AUTHORIZATION_HEADER, this.config.key())
                    .POST(HttpRequest.BodyPublishers.ofString(this.mapper.writeValueAsString(products)))
                    .build();

            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return Objects.requireNonNullElse(this.mapper.readValue(response.body(), BrinkProductResponseWrapper.class).products(), new ArrayList<>());

        } catch (Exception e) {
            throw new BrinkException("Could not create product", e);
        }
    }

    public Optional<BrinkProduct> update(final BrinkProduct product) {
        return Optional.ofNullable(update(List.of(product)).get(0));
    }

    public List<BrinkProduct> update(final List<BrinkProduct> products) {
        Objects.requireNonNull(products, "Can not create a product of null");
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.config.host() + "/productv1/products"))
                    .header("Content-Type", CONTENT_TYPE)
                    .headers(BrinkConfiguration.AUTHORIZATION_HEADER, this.config.key())
                    .PUT(HttpRequest.BodyPublishers.ofString(this.mapper.writeValueAsString(products)))
                    .build();
            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Objects.requireNonNullElse(this.mapper.readValue(response.body(), BrinkProductResponseWrapper.class).products(), new ArrayList<>());
        } catch (Exception e) {
            throw new BrinkException("Could not create product", e);
        }
    }



    public Optional<BrinkProduct> delete(final BrinkProduct product) {
        Objects.requireNonNull(product, "Can not delete a product of null");
        return this.delete(product.id());
    }

    public Optional<BrinkProduct> delete(final String productId) {
        Objects.requireNonNull(productId, "Can not delete a product without id");
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.productUrl + productId))
                    .header("Content-Type", BrinkConfiguration.CONTENT_TYPE)
                    .headers(BrinkConfiguration.AUTHORIZATION_HEADER, this.config.key())
                    .DELETE()
                    .build();

            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return Optional.ofNullable(this.mapper.readValue(response.body(), BrinkProduct.class));
        } catch (Exception e) {
            throw new BrinkException("Could not create product", e);
        }
    }

    public BrinkProductApi maxBulkSize(Integer maxBulkSize) {
        if(0 > Objects.requireNonNull(maxBulkSize, "maxBulkSize cannot be null")) {
            throw new BrinkStoreException("maxBulkSize needs to be a positive integer");
        }
        this.maxBulkSize = maxBulkSize;
        return this;
    }

    public BrinkProductSearchFilter filter() {
        return BrinkProductSearchFilter.create();
    }
}
