package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.Assertion;

public record ReadAssertionsResponse(
        @JsonProperty("authorization_model_id") String authorizationModelId,
        List<Assertion> assertions) {

    public ReadAssertionsResponse {
        Objects.requireNonNull(authorizationModelId, "authorizationModelId cannot be null");
        Objects.requireNonNull(assertions, "assertions cannot be null");
    }
}
