package io.quarkiverse.openfga.client.model;

import java.util.List;
import java.util.Objects;

public record Usersets(
        List<Userset> child) {

    public Usersets {
        Objects.requireNonNull(child, "child cannot be null");
    }
}
