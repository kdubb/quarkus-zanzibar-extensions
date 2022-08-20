package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.Tuple;

public record ReadTuplesResponse(
        List<Tuple> tuples,
        @JsonProperty("continuation_token") @Nullable String continuationToken) {

    public ReadTuplesResponse {
        Objects.requireNonNull(tuples, "tuples cannot be null");
    }
}