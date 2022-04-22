package com.brinkcommerce.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;

public record BrinkConfiguration(String key, String host, Integer port, Integer timeoutInSeconds, ObjectMapper mapper, HttpClient httpClient) {

    public static final String AUTHORIZATION_HEADER = "x-api-key";
    public static final String CONTENT_TYPE = "application/json";
    public static final Integer DEFAULT_MAX_BULK_SIZE = 200;

    public static BrinkConfigurationBuilder builder() {
        return BrinkConfigurationBuilder.create();
    }
}
