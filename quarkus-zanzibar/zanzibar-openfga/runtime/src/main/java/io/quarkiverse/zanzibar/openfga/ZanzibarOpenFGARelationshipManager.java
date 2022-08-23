package io.quarkiverse.zanzibar.openfga;

import static java.lang.String.format;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.model.PartialTupleKey;
import io.quarkiverse.openfga.client.model.Tuple;
import io.quarkiverse.openfga.client.model.TupleKey;
import io.quarkiverse.zanzibar.Relationship;
import io.quarkiverse.zanzibar.RelationshipManager;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class ZanzibarOpenFGARelationshipManager implements RelationshipManager {

    private final AuthorizationModelClient authorizationModelClient;

    @Inject
    public ZanzibarOpenFGARelationshipManager(AuthorizationModelClient authorizationModelClient) {
        this.authorizationModelClient = authorizationModelClient;
    }

    public Uni<Boolean> check(Relationship relationship) {

        return authorizationModelClient.check(tupleKeyFromRelationship(relationship), null);
    }

    @Override
    public Uni<Void> add(List<Relationship> relationships) {

        var tuples = relationships.stream()
                .map(this::tupleKeyFromRelationship)
                .collect(Collectors.toList());

        return authorizationModelClient.write(tuples, null)
                .replaceWithVoid();
    }

    @Override
    public Uni<Void> remove(List<Relationship> relationships) {

        var tuples = relationships.stream()
                .map(this::tupleKeyFromRelationship)
                .collect(Collectors.toList());

        return authorizationModelClient.write(null, tuples)
                .replaceWithVoid();
    }

    @Override
    public Uni<List<Relationship>> findAllRelationshipsForObject(String objectType, String objectId) {

        return authorizationModelClient.queryAllTuples(PartialTupleKey.of(object(objectType, objectId), null, null))
                .map(results -> results.stream().map(this::relationshipFromTuple).collect(Collectors.toList()));
    }

    @Override
    public Uni<List<Relationship>> findAllRelationshipsForObjectAndUser(String objectType, String objectId, String user) {

        return authorizationModelClient.queryAllTuples(PartialTupleKey.of(object(objectType, objectId), null, user))
                .map(results -> results.stream().map(this::relationshipFromTuple).collect(Collectors.toList()));
    }

    @Override
    public Uni<List<Relationship>> findAllRelationshipsForObjectAndRelation(String objectType, String objectId,
            String relation) {

        return authorizationModelClient.queryAllTuples(PartialTupleKey.of(object(objectType, objectId), relation, null))
                .map(results -> results.stream().map(this::relationshipFromTuple).collect(Collectors.toList()));
    }

    @Override
    public Uni<List<Relationship>> findAllRelationshipsForObjectTypeAndUser(String objectType, String user) {

        return authorizationModelClient.queryAllTuples(PartialTupleKey.of(objectType + ":", null, user))
                .map(results -> results.stream().map(this::relationshipFromTuple).collect(Collectors.toList()));
    }

    @Override
    public Uni<List<String>> findAllObjects(String objectType, String relation, String user) {

        return authorizationModelClient.listObjects(objectType, relation, user, null);
    }

    String object(String objectType, String objectId) {
        return format("%s:%s", objectType, objectId);
    }

    TupleKey tupleKeyFromRelationship(Relationship relationship) {
        return TupleKey.of(
                object(relationship.getObjectType(), relationship.getObjectId()),
                relationship.getRelation(),
                relationship.getUseId());
    }

    Relationship relationshipFromTuple(Tuple tuple) {

        var parts = tuple.getKey().getObject().split(":", 1);

        return new Relationship(parts[0], parts[1], tuple.getKey().getRelation(), tuple.getKey().getObject());
    }
}
