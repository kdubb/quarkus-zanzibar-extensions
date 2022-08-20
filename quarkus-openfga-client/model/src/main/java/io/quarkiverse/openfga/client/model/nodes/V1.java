package io.quarkiverse.openfga.client.model.nodes;

import java.util.Objects;

import io.quarkiverse.openfga.client.model.ObjectRelation;
import io.quarkiverse.openfga.client.model.Userset;

public class V1 {

    public record TupleToUserset(
            ObjectRelation tupleset,
            ObjectRelation computedUserset) {

        public TupleToUserset {
            Objects.requireNonNull(tupleset, "tupleset cannot be null");
            Objects.requireNonNull(computedUserset, "computedUserset cannot be null");
        }
    }

    public record Difference(
            Userset base,
            Userset subtract) {

        public Difference {
            Objects.requireNonNull(base, "base cannot be null");
            Objects.requireNonNull(subtract, "subtract cannot be null");
        }
    }

}
