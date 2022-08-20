package io.quarkiverse.openfga.client.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TupleChange(
        @JsonProperty("tuple_key") TupleKey tupleKey,
        TupleOperation operation,
        OffsetDateTime timestamp) {

    public TupleChange {
        Objects.requireNonNull(tupleKey, "tupleKey cannot be null");
        Objects.requireNonNull(operation, "operation cannot be null");
        Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }
}
