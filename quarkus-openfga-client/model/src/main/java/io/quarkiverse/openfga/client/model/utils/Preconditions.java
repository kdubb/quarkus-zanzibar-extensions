package io.quarkiverse.openfga.client.model.utils;

import java.util.Objects;

import javax.annotation.Nonnull;

public class Preconditions {

    public static <T> @Nonnull T parameterNonNull(@Nonnull T value, @Nonnull String name) {
        return Objects.requireNonNull(value, name + " parameter must not be null");
    }

}
