package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.Store;

public record ListStoresResponse(
        List<Store> stores,
        @JsonProperty("continuation_token") @Nullable String continuationToken) {

    public ListStoresResponse {
        Objects.requireNonNull(stores, "stores cannot be null");
    }
}
