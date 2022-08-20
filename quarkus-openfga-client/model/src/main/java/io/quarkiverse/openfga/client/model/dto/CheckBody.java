package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.ContextualTupleKeys;
import io.quarkiverse.openfga.client.model.TupleKey;

public record CheckBody(
        @JsonProperty("tuple_key") TupleKey tupleKey,
        @JsonProperty("contextual_tuples") @Nullable ContextualTupleKeys contextualTupleKeys,
        @JsonProperty("authorization_model_id") String authorizationModelId,
        @Nullable Boolean trace) {

    public CheckBody {
        Objects.requireNonNull(tupleKey, "tupleKey cannot be null");
        Objects.requireNonNull(authorizationModelId, "authorizationModelId cannot be null");
    }
}
