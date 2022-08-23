package io.quarkiverse.zanzibar;

import java.util.List;

import io.smallrye.mutiny.Uni;

public interface RelationshipManager {

    Uni<Boolean> check(Relationship relationship);

    Uni<Void> add(List<Relationship> relationships);

    Uni<Void> remove(List<Relationship> relationships);

    Uni<List<Relationship>> findAllRelationshipsForObject(String objectType, String objectId);

    Uni<List<Relationship>> findAllRelationshipsForObjectAndUser(String objectType, String objectId, String user);

    Uni<List<Relationship>> findAllRelationshipsForObjectAndRelation(String objectType, String objectId, String relation);

    Uni<List<Relationship>> findAllRelationshipsForObjectTypeAndUser(String objectType, String user);

    Uni<List<String>> findAllObjects(String objectType, String relation, String user);

}
