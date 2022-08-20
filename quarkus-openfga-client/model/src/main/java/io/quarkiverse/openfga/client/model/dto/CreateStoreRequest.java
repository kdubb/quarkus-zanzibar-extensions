package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

public record CreateStoreRequest(
        String name) {

    public CreateStoreRequest {
        Objects.requireNonNull(name, "name cannot be null");
    }
}
