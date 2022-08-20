package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.TupleKey;

public record ExpandBody(
        @JsonProperty("tuple_key") TupleKey tupleKey,
        @JsonProperty("authorization_model_id") String authorizationModelId) {

    public ExpandBody {
        Objects.requireNonNull(tupleKey, "tupleKey cannot be null");
        Objects.requireNonNull(authorizationModelId, "authorizationModelId cannot be null");
    }
}
