package io.quarkiverse.openfga.client.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.nodes.V1;

public record Userset(
        @Nullable @JsonProperty("this") DirectUserset directUserset,
        @Nullable ObjectRelation computedUserset,
        @Nullable V1.TupleToUserset tupleToUserset,
        @Nullable Usersets union,
        @Nullable Usersets intersection,
        @Nullable V1.Difference difference) {

    public static Userset direct() {
        return new Userset(new DirectUserset(), null, null, null, null, null);
    }

    public static Userset computed(ObjectRelation computedUserset) {
        return new Userset(null, computedUserset, null, null, null, null);
    }

    public static Userset tupleTo(V1.TupleToUserset tupleToUserset) {
        return new Userset(null, null, tupleToUserset, null, null, null);
    }

    public static Userset union(Usersets union) {
        return new Userset(null, null, null, union, null, null);
    }

    public static Userset intersection(Usersets intersection) {
        return new Userset(null, null, null, null, intersection, null);
    }

    public static Userset difference(V1.Difference difference) {
        return new Userset(null, null, null, null, null, difference);
    }
}
