package io.quarkiverse.openfga.client.model;

import java.util.Map;
import java.util.Objects;

public record TypeDefinition(
        String type,
        Map<String, Userset> relations) {

    public TypeDefinition {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(relations, "relations cannot be null");
    }
}
