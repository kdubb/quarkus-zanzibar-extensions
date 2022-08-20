package io.quarkiverse.openfga.client.model.dto;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReadTuplesBody(
        @JsonProperty("page_size") @Nullable Integer pageSize,
        @JsonProperty("continuation_token") @Nullable String continuationToken) {
}
