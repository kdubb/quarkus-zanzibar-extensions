package io.quarkiverse.zanzibar.jaxrs;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.time.Duration;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import io.quarkiverse.zanzibar.Relationship;
import io.quarkiverse.zanzibar.RelationshipManager;

public class ZanzibarSynchronousAuthorizationFilter extends ZanzibarAuthorizationFilter implements ContainerRequestFilter {

    private static final Logger log = Logger.getLogger(ZanzibarSynchronousAuthorizationFilter.class);

    public ZanzibarSynchronousAuthorizationFilter(Action action,
            RelationshipManager relationshipManager, Optional<String> unauthenticatedUser, Duration timeout) {
        super(action, relationshipManager, unauthenticatedUser, timeout);
    }

    @Override
    public void filter(ContainerRequestContext context) {

        var checkOpt = prepare(context);

        if (checkOpt.isEmpty()) {
            context.abortWith(Response.status(FORBIDDEN).build());
            return;
        }
        var check = checkOpt.get();

        try {

            var relationship = Relationship.of(check.objectType, check.objectId, check.relation, check.user);

            var allowed = relationshipManager.check(relationship)
                    .await().atMost(Duration.ofSeconds(10));

            log.debugf("Authorization %s", allowed ? "allowed" : "disallowed");

            if (!allowed) {
                context.abortWith(Response.status(FORBIDDEN).build());
            }

        } catch (Throwable x) {

            log.error("Authorization check failed", x);

            context.abortWith(Response.status(INTERNAL_SERVER_ERROR).build());

        }
    }
}
