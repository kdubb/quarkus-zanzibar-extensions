package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.TupleKey;

public record ReadBody(
        @JsonProperty("tuple_key") TupleKey tupleKey,
        @JsonProperty("authorization_model_id") String authorizationModelId,
        @JsonProperty("page_size") @Nullable Integer pageSize,
        @JsonProperty("continuation_token") @Nullable String continuationToken) {

    public ReadBody {
        Objects.requireNonNull(tupleKey, "tupleKey cannot be null");
        Objects.requireNonNull(authorizationModelId, "authorizationModelId cannot be null");
    }
}
