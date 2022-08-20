package io.quarkiverse.openfga.client.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorizationModel(
        String id,
        @JsonProperty("type_definitions") List<TypeDefinition> typeDefinitions) {

    public AuthorizationModel {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(typeDefinitions, "typeDefinitions cannot be null");
    }

}
