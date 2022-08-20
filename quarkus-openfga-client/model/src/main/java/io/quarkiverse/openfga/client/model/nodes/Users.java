package io.quarkiverse.openfga.client.model.nodes;

import java.util.List;
import java.util.Objects;

public record Users(
        List<String> users) {

    public Users {
        Objects.requireNonNull(users, "users cannot be null");
    }
}
