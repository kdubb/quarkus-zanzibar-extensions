package io.quarkiverse.zanzibar;

import io.smallrye.mutiny.Uni;

public interface Authorizer {

    Uni<Boolean> check(String objectType, String objectId, String relation, String user);

}
