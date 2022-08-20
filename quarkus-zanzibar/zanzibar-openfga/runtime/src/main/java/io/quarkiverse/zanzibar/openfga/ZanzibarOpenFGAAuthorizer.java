package io.quarkiverse.zanzibar.openfga;

import static java.lang.String.format;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.model.TupleKey;
import io.quarkiverse.zanzibar.Authorizer;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class ZanzibarOpenFGAAuthorizer implements Authorizer {

    private final AuthorizationModelClient authorizationModelClient;

    @Inject
    public ZanzibarOpenFGAAuthorizer(AuthorizationModelClient authorizationModelClient) {
        this.authorizationModelClient = authorizationModelClient;
    }

    public Uni<Boolean> check(String objectType, String objectId, String relation, String user) {

        var checkTuple = TupleKey.of(format("%s:%s", objectType, objectId), relation, user);

        return authorizationModelClient.check(checkTuple, null);
    }

}
