package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import io.quarkiverse.openfga.client.model.Assertion;

public record WriteAssertionsRequest(
        List<Assertion> assertions) {

    public WriteAssertionsRequest {
        Objects.requireNonNull(assertions, "assertions cannot be null");
    }
}
