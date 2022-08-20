package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.ContextualTupleKeys;

public record ListObjectsBody(
        @JsonProperty("authorization_model_id") String authorizationModelId,
        String type,
        String relation,
        String user,
        @JsonProperty("contextual_tuples") ContextualTupleKeys contextualTupleKeys) {

    public ListObjectsBody {
        Objects.requireNonNull(authorizationModelId, "authorizationModelId cannot be null");
        Objects.requireNonNull(type, "type cannot be type");
        Objects.requireNonNull(relation, "relation cannot be type");
        Objects.requireNonNull(user, "user cannot be type");
        Objects.requireNonNull(contextualTupleKeys, "contextualTupleKeys cannot be type");
    }
}
