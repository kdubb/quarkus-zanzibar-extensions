package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.AuthorizationModel;

public record ReadAuthorizationModelResponse(
        @JsonProperty("authorization_model") AuthorizationModel authorizationModel) {

    public ReadAuthorizationModelResponse {
        Objects.requireNonNull(authorizationModel, "authorizationModel cannot be null");
    }
}
