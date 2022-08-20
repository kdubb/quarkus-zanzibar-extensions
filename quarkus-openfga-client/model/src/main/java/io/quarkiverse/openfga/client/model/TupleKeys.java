package io.quarkiverse.openfga.client.model;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TupleKeys(
        @JsonProperty("tuple_keys") List<TupleKey> tupleKeys) {

    public TupleKeys {
        Objects.requireNonNull(tupleKeys, "tupleKeys cannot be null");
        if (tupleKeys.isEmpty()) {
            throw new IllegalStateException("tupleKeys requires a minimum of 1 item");
        }
    }

    public static TupleKeys of(@Nullable List<TupleKey> tupleKeys) {
        if (tupleKeys == null || tupleKeys.isEmpty()) {
            return null;
        }
        return new TupleKeys(tupleKeys);
    }
}
