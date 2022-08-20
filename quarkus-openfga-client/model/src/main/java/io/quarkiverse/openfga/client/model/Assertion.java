package io.quarkiverse.openfga.client.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Assertion(
        @JsonProperty("tuple_key") TupleKey tupleKey,
        boolean expectation) {

    public Assertion {
        Objects.requireNonNull(tupleKey, "tupleKey cannot be null");
    }
}
