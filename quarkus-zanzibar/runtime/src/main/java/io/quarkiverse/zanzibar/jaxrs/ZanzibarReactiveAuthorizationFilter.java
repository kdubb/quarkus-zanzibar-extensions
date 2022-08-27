package io.quarkiverse.zanzibar.jaxrs;

import static javax.ws.rs.Priorities.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.spi.ResteasyReactiveContainerRequestContext;
import org.jboss.resteasy.reactive.server.spi.ResteasyReactiveContainerRequestFilter;

import io.quarkiverse.zanzibar.Relationship;

@ApplicationScoped
@Provider
@Priority(AUTHORIZATION)
public class ZanzibarReactiveAuthorizationFilter extends ZanzibarAuthorizationFilter
        implements ResteasyReactiveContainerRequestFilter {

    private static final Logger log = Logger.getLogger(ZanzibarReactiveAuthorizationFilter.class);

    @Override
    public void filter(ResteasyReactiveContainerRequestContext context) {

        var checkResult = prepare(context);

        if (checkResult instanceof Result.NoCheck) {

            log.debug("Allowing request without check");

        } else if (checkResult instanceof Result.Forbidden) {

            log.debug("Denying request");

            context.abortWith(Response.status(FORBIDDEN).build());

        } else if (checkResult instanceof Result.Check) {

            var check = (Result.Check) checkResult;

            context.suspend();

            log.debugf(
                    "Authorizing object-type=%s, object-id=%s, relation=%s, user=%s",
                    check.type, check.object, check.relation, check.user);

            relationshipManager.check(Relationship.of(check.type, check.object, check.relation, check.user))
                    .ifNoItem().after(timeout).fail()
                    .subscribe().with((allowed) -> {

                        log.debugf("Authorization %s", allowed ? "allowed" : "disallowed");

                        if (!allowed) {

                            context.abortWith(Response.status(FORBIDDEN).build());
                        }

                        context.resume();

                    }, (x) -> {
                        log.error("Authorization check failed", x);

                        context.abortWith(Response.status(INTERNAL_SERVER_ERROR).build());

                        context.resume();
                    });

        } else {

            throw new IllegalStateException("Unsupported authorization result");

        }
    }
}
