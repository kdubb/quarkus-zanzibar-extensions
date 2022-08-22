package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.utils.Preconditions;

public final class ListObjectsResponse {
    @JsonProperty("object_ids")
    private final List<String> objectIds;

    public ListObjectsResponse(@JsonProperty("object_ids") List<String> objectIds) {
        this.objectIds = Preconditions.parameterNonNull(objectIds, "objectIds");
    }

    @JsonProperty("object_ids")
    public List<String> getObjectIds() {
        return objectIds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (ListObjectsResponse) obj;
        return Objects.equals(this.objectIds, that.objectIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectIds);
    }

    @Override
    public String toString() {
        return "ListObjectsResponse[" +
                "objectIds=" + objectIds + ']';
    }

}
