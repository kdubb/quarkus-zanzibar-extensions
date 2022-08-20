package io.quarkiverse.openfga.client.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TypeDefinitions(
        @JsonProperty("type_definitions") List<TypeDefinition> typeDefinitions) {

    public TypeDefinitions {
        Objects.requireNonNull(typeDefinitions, "typeDefinitions cannot be null");
    }
}
