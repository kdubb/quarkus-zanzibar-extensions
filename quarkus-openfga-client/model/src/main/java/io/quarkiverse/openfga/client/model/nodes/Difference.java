package io.quarkiverse.openfga.client.model.nodes;

import java.util.Objects;

public record Difference(
        Node base,
        Node subtract) {

    public Difference {
        Objects.requireNonNull(base, "base cannot be null");
        Objects.requireNonNull(subtract, "subtract cannot be null");
    }
}
