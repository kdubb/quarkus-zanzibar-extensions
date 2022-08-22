package io.quarkiverse.openfga.client.model.dto;

import java.util.Objects;

import io.quarkiverse.openfga.client.model.utils.Preconditions;

public final class CreateStoreRequest {
    private final String name;

    public CreateStoreRequest(String name) {
        this.name = Preconditions.parameterNonNull(name, "name");
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (CreateStoreRequest) obj;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "CreateStoreRequest[" +
                "name=" + name + ']';
    }

}
