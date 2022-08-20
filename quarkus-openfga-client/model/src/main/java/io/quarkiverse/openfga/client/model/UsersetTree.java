package io.quarkiverse.openfga.client.model;

import java.util.Objects;

import io.quarkiverse.openfga.client.model.nodes.Node;

public record UsersetTree(
        Node root) {

    public UsersetTree {
        Objects.requireNonNull(root, "root cannot be null");
    }
}
