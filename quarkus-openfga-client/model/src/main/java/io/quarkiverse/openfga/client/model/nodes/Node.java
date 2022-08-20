package io.quarkiverse.openfga.client.model.nodes;

import java.util.Objects;

import javax.annotation.Nullable;

public record Node(
        String name,
        @Nullable Leaf leaf,
        @Nullable Difference difference,
        @Nullable Nodes union,
        @Nullable Nodes intersection) {

    public Node {
        Objects.requireNonNull(name, "name cannot be null");
    }
}
