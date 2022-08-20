package io.quarkiverse.openfga.client.utils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public record PaginatedList<T> (
        List<T> items,
        @Nullable String token) {
    public PaginatedList {
        Objects.requireNonNull(items, "items cannot be null");
    }

    public Boolean isLastPage() {
        return token != null && !token.isEmpty();
    }

    public static <T> Uni<List<T>> collectAllPages(@Nullable Integer pageSize,
            BiFunction<Integer, String, Uni<PaginatedList<T>>> listGenerator) {
        return Multi.createBy()
                .repeating().uni(AtomicReference<String>::new, lastToken -> {
                    return listGenerator.apply(pageSize, lastToken.get())
                            .onItem().invoke(list -> lastToken.set(list.token()));
                })
                .whilst(PaginatedList::isLastPage)
                .onItem().transformToIterable(PaginatedList::items)
                .collect().asList();
    }

}
