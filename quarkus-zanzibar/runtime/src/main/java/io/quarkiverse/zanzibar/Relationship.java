package io.quarkiverse.zanzibar;

import java.util.Objects;

public class Relationship {

    private String objectType;
    private String objectId;
    private String relation;
    private String useId;

    public Relationship(String objectType, String objectId, String relation, String useId) {
        this.objectType = objectType;
        this.objectId = objectId;
        this.relation = relation;
        this.useId = useId;
    }

    public static Relationship of(String objectType, String objectId, String relation, String useId) {
        return new Relationship(objectType, objectId, relation, useId);
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Relationship))
            return false;
        Relationship that = (Relationship) o;
        return Objects.equals(objectType, that.objectType) && Objects.equals(objectId, that.objectId)
                && Objects.equals(relation, that.relation) && Objects.equals(useId, that.useId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, objectId, relation, useId);
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "objectType='" + objectType + '\'' +
                ", objectId='" + objectId + '\'' +
                ", relation='" + relation + '\'' +
                ", useId='" + useId + '\'' +
                '}';
    }
}
