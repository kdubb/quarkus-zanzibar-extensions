package io.quarkiverse.openfga.client.model.nodes;

import java.util.List;
import java.util.Objects;

public record TupleToUserset(
        String tupleset,
        List<Computed> computed) {

    public TupleToUserset {
        Objects.requireNonNull(tupleset, "tupleset cannot be null");
        Objects.requireNonNull(computed, "computed cannot be null");
    }
}
