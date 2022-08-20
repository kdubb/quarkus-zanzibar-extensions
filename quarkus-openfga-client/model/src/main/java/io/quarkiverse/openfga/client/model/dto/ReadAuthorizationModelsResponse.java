package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.AuthorizationModel;

public record ReadAuthorizationModelsResponse(
        @JsonProperty("authorization_models") List<AuthorizationModel> authorizationModels,
        @JsonProperty("continuation_token") @Nullable String continuationToken) {

    public ReadAuthorizationModelsResponse {
        Objects.requireNonNull(authorizationModels, "authorizationModels cannot be null");
    }
}
