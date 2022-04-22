package com.brinkcommerce.api.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;

public class BrinkConfigurationBuilder {

    private static final Integer DEFAULT_PORT = 80;
    private static final Integer DEFAULT_TIMEOUT_IN_SECONDS = 20;

    private String key;
    private String url;
    private Integer port;
    private Integer timeout;
    private ObjectMapper mapper;
    private HttpClient httpClient;

    private BrinkConfigurationBuilder() {
        this.port = DEFAULT_PORT;
        this.timeout = DEFAULT_TIMEOUT_IN_SECONDS;
        this.mapper = defaultMapper();
        this.httpClient = defaultHttpClient();
    }

    public static BrinkConfigurationBuilder create() {
        return new BrinkConfigurationBuilder();
    }

    public BrinkConfiguration build() {
        return new BrinkConfiguration(this.key, this.url, this.port, this.timeout, this.mapper, this.httpClient);
    }


    public BrinkConfigurationBuilder withMapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public BrinkConfigurationBuilder withKey(String key) {
        this.key = key;
        return this;
    }

    public BrinkConfigurationBuilder withTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    public BrinkConfigurationBuilder withPort(Integer port) {
        this.port = Objects.requireNonNull(port);
        return this;
    }

    public BrinkConfigurationBuilder withHost(String url) {
        this.url = url;
        return this;
    }

    public BrinkConfigurationBuilder withHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    private ObjectMapper defaultMapper() {
        if(Objects.isNull(this.mapper)) {
            this.mapper = new ObjectMapper()
                    .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL); //TODO: Replace this deprecated module
        }
        return this.mapper;
    }

    private HttpClient defaultHttpClient() {
        if(Objects.isNull(this.httpClient)) {
            this.httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(this.timeout))
                    .proxy(ProxySelector.getDefault())
                    .build();
        }
        return this.httpClient;
    }
}
