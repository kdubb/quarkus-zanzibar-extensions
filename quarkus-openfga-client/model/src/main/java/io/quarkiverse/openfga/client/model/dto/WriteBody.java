package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.TupleKeys;

public record WriteBody(
        @Nullable TupleKeys writes,
        @Nullable TupleKeys deletes,
        @JsonProperty("authorization_model_id") String authorizationModelId) {

    public WriteBody {
        Objects.requireNonNull(authorizationModelId, "authorizationModelId cannot be null");
    }
}
