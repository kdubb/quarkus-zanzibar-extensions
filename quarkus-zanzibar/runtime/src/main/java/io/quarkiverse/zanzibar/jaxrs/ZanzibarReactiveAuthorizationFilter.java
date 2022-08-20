package io.quarkiverse.zanzibar.jaxrs;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.spi.ResteasyReactiveContainerRequestContext;
import org.jboss.resteasy.reactive.server.spi.ResteasyReactiveContainerRequestFilter;

@ApplicationScoped
@Provider
public class ZanzibarReactiveAuthorizationFilter extends ZanzibarAuthorizationFilter
        implements ResteasyReactiveContainerRequestFilter {

    private static final Logger log = Logger.getLogger(ZanzibarReactiveAuthorizationFilter.class);

    @Override
    public void filter(ResteasyReactiveContainerRequestContext context) {

        var checkResult = prepare(context);

        if (checkResult instanceof Result.NoCheck) {

            // No check needed

        } else if (checkResult instanceof Result.Forbidden) {

            context.abortWith(Response.status(FORBIDDEN).build());

        } else if (checkResult instanceof Result.Check check) {

            context.suspend();

            authorizer.check(check.type(), check.object(), check.relation(), check.user())
                    .ifNoItem().after(timeout).fail()
                    .subscribe().with((allowed) -> {

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
