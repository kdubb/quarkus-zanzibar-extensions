package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import io.quarkiverse.openfga.client.model.TupleChange;

public record ReadChangesResponse(
        List<TupleChange> changes) {

    public ReadChangesResponse {
        Objects.requireNonNull(changes, "changes cannot be null");
    }
}
