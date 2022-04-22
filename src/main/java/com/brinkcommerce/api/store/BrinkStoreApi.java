package com.brinkcommerce.api.store;

import com.brinkcommerce.api.configuration.BrinkConfiguration;
import com.brinkcommerce.api.exception.BrinkException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BrinkStoreApi {

    private static final String CONTENT_TYPE = "application/json";
    private static final String PATH = "/productv1/stores/";

    private final BrinkConfiguration config;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;
    private final URI uri;
    private Integer maxBulkSize;

    private BrinkStoreApi(BrinkConfiguration config) {
        this.config = Objects.requireNonNull(config, "Configuration must be provided");
        this.mapper = Objects.requireNonNull(config.mapper(), "Objectmapper must be provided");
        this.httpClient = Objects.requireNonNull(config.httpClient(), "HttpClient cannot be null");
        this.uri = URI.create(this.config.host() + PATH);
        this.maxBulkSize = 1000;
    }

    public static BrinkStoreApi create(BrinkConfiguration config) {
        return new BrinkStoreApi(config);
    }

    public BrinkStore update(final BrinkStore store) {
        Objects.requireNonNull(store, "BrinkStore can not be null when updating store");
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(this.uri)
                    .header("Content-Type", CONTENT_TYPE)
                    .headers(BrinkConfiguration.AUTHORIZATION_HEADER, this.config.key())
                    .PUT(HttpRequest.BodyPublishers.ofString(this.mapper.writeValueAsString(store)))
                    .build();

            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return this.mapper.readValue(response.body(), BrinkStore.class);
        } catch (Exception e) {
            throw new BrinkException("Could not update stock", e);
        }
    }

    public List<BrinkStore> get(BrinkCountryCode... countryCodes) {
        Objects.requireNonNull(countryCodes, "BrinkCountryCode array can not be null when fetching stores");
        return get(Arrays.asList(countryCodes));
    }

    public List<BrinkStore> get(List<BrinkCountryCode> countryCodes) {
        Objects.requireNonNull(countryCodes, "BrinkCountryCodes can not be null when fetching stores");
        final String countryCodesAsString = countryCodesAsString(countryCodes);

        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(this.uri)
                    .header("Content-Type", CONTENT_TYPE)
                    .headers(BrinkConfiguration.AUTHORIZATION_HEADER, this.config.key())
                    .PUT(HttpRequest.BodyPublishers.ofString(this.mapper.writeValueAsString(null)))
                    .build();

            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return new ArrayList<>();
        } catch (Exception e) {
            throw new BrinkException("Could not update stock", e);
        }
    }

    private String countryCodesAsString(List<BrinkCountryCode> countryCodes) {
        return countryCodes.stream()
                .map(BrinkCountryCode::alpha2)
                .collect(Collectors.joining(","));
    }

    public List<BrinkStore> getAll() {
        return get(Arrays.asList(BrinkCountryCode.values()));
    }

    public BrinkStoreApi maxBulkSize(Integer maxBulkSize) {
        if(0 > Objects.requireNonNull(maxBulkSize, "maxBulkSize cannot be null")) {
            throw new BrinkStoreException("maxBulkSize needs to be a positive integer");
        }
        this.maxBulkSize = maxBulkSize;
        return this;
    }
}
