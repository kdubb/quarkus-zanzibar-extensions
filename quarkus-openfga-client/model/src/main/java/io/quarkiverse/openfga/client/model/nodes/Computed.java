package io.quarkiverse.openfga.client.model.nodes;

import java.util.Objects;

public record Computed(
        String userset) {

    public Computed {
        Objects.requireNonNull(userset, "userset cannot be null");
    }
}
