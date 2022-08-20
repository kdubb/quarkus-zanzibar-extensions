package io.quarkiverse.openfga.client.model;

import java.util.Objects;

public record TupleKey(
        String object,
        String relation,
        String user) {

    public TupleKey {
        Objects.requireNonNull(object, "object cannot be null");
        Objects.requireNonNull(relation, "relation cannot be null");
        Objects.requireNonNull(user, "user cannot be null");
    }

    public static TupleKey of(String object, String relation, String user) {
        return new TupleKey(object, relation, user);
    }
}
