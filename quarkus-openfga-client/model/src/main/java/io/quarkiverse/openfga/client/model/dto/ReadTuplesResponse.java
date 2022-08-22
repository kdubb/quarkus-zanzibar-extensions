package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.Tuple;
import io.quarkiverse.openfga.client.model.utils.Preconditions;

public final class ReadTuplesResponse {
    private final List<Tuple> tuples;
    @JsonProperty("continuation_token")
    @Nullable
    private final String continuationToken;

    public ReadTuplesResponse(List<Tuple> tuples, @JsonProperty("continuation_token") @Nullable String continuationToken) {
        this.tuples = Preconditions.parameterNonNull(tuples, "tuples");
        this.continuationToken = continuationToken;
    }

    public List<Tuple> getTuples() {
        return tuples;
    }

    @JsonProperty("continuation_token")
    @Nullable
    public String getContinuationToken() {
        return continuationToken;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (ReadTuplesResponse) obj;
        return Objects.equals(this.tuples, that.tuples) &&
                Objects.equals(this.continuationToken, that.continuationToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tuples, continuationToken);
    }

    @Override
    public String toString() {
        return "ReadTuplesResponse[" +
                "tuples=" + tuples + ", " +
                "continuationToken=" + continuationToken + ']';
    }

}
