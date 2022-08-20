package io.quarkiverse.openfga.client.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContextualTupleKeys(
        @JsonProperty("tuple_keys") List<TupleKey> tupleKeys) {

    public ContextualTupleKeys {
        Objects.requireNonNull(tupleKeys, "tupleKeys cannot be null");
    }
}
