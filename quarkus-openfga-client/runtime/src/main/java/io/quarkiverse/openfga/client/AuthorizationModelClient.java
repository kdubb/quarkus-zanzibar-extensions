package io.quarkiverse.openfga.client;

import static io.quarkiverse.openfga.client.utils.PaginatedList.collectAllPages;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import io.quarkiverse.openfga.client.api.API;
import io.quarkiverse.openfga.client.model.*;
import io.quarkiverse.openfga.client.model.dto.*;
import io.quarkiverse.openfga.client.utils.PaginatedList;
import io.smallrye.mutiny.Uni;

public class AuthorizationModelClient {

    private final API api;
    private final String storeId;
    private final String authorizationModelId;

    public AuthorizationModelClient(API api, String storeId, String authorizationModelId) {
        this.api = api;
        this.storeId = storeId;
        this.authorizationModelId = authorizationModelId;
    }

    public Uni<AuthorizationModel> get() {
        return api.readAuthorizationModel(storeId, authorizationModelId)
                .map(ReadAuthorizationModelResponse::authorizationModel);
    }

    public Uni<Boolean> check(TupleKey tupleKey, @Nullable ContextualTupleKeys contextualTupleKeys) {
        return api.check(storeId, new CheckBody(tupleKey, contextualTupleKeys, authorizationModelId, null))
                .map(CheckResponse::allowed);
    }

    public Uni<UsersetTree> expand(TupleKey tupleKey) {
        return api.expand(storeId, new ExpandBody(tupleKey, authorizationModelId))
                .map(ExpandResponse::tree);
    }

    public Uni<List<String>> listObjects(String type, String relation, String user, List<TupleKey> contextualTupleKeys) {
        return api
                .listObjects(storeId,
                        new ListObjectsBody(authorizationModelId, type, relation, user,
                                new ContextualTupleKeys(contextualTupleKeys)))
                .map(ListObjectsResponse::objectIds);
    }

    public Uni<PaginatedList<Tuple>> queryTuples(TupleKey tupleKey, @Nullable Integer pageSize, @Nullable String pagingToken) {
        return api.read(storeId, new ReadBody(tupleKey, authorizationModelId, pageSize, pagingToken))
                .map(res -> new PaginatedList<>(res.tuples(), res.continuationToken()));
    }

    public Uni<List<Tuple>> queryAllTuples(TupleKey tupleKey) {
        return queryAllTuples(tupleKey, null);
    }

    public Uni<List<Tuple>> queryAllTuples(TupleKey tupleKey, @Nullable Integer pageSize) {
        return collectAllPages(pageSize, (currentPageSize, currentToken) -> {
            return queryTuples(tupleKey, currentPageSize, currentToken);
        });
    }

    public Uni<Void> write(TupleKey tupleKey) {
        return write(List.of(tupleKey), null)
                .replaceWithVoid();
    }

    public Uni<Map<String, Object>> write(@Nullable List<TupleKey> writes, @Nullable List<TupleKey> deletes) {
        return api.write(storeId, new WriteBody(TupleKeys.of(writes), TupleKeys.of(deletes), authorizationModelId))
                .map(Function.identity());
    }

}
