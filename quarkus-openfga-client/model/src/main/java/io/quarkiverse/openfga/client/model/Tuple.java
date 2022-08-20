package io.quarkiverse.openfga.client.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public record Tuple(
        TupleKey key,
        OffsetDateTime timestamp) {

    public Tuple {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }
}
