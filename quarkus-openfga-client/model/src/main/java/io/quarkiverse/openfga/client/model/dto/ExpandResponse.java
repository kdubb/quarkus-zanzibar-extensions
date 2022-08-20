package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import io.quarkiverse.openfga.client.model.UsersetTree;

public record ExpandResponse(
        UsersetTree tree) {

    public ExpandResponse {
        Objects.requireNonNull(tree, "tree cannot be null");
    }
}
