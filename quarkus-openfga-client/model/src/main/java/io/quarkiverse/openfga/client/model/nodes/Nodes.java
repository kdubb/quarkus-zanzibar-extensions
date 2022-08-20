package io.quarkiverse.openfga.client.model.nodes;

import java.util.List;
import java.util.Objects;

public record Nodes(
        List<Node> nodes) {

    public Nodes {
        Objects.requireNonNull(nodes, "nodes cannot be null");
    }
}
