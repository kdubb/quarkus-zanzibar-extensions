package io.quarkiverse.openfga.client.model;

import java.util.Objects;

public record ObjectRelation(
        String object,
        String relation) {

    public ObjectRelation {
        Objects.requireNonNull(object, "object cannot be null");
        Objects.requireNonNull(relation, "relation cannot be null");
    }
}
