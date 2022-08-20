package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WriteAuthorizationModelResponse(
        @JsonProperty("authorization_model_id") String authorizationModelId) {

    public WriteAuthorizationModelResponse {
        Objects.requireNonNull(authorizationModelId, "authorizationModelId cannot be null");
    }
}
