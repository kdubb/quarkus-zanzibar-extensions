package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListObjectsResponse(
        @JsonProperty("object_ids") List<String> objectIds) {

    public ListObjectsResponse {
        Objects.requireNonNull(objectIds, "objectIds cannot be null");
    }
}
