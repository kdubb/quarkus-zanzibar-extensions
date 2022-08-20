package io.quarkiverse.openfga.client.model.nodes;

import javax.annotation.Nullable;

public record Leaf(
        @Nullable Users users,
        @Nullable Computed computed,
        @Nullable TupleToUserset tupleToUserset) {
}
