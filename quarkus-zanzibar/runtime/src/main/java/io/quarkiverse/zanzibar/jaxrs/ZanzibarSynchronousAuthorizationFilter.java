package io.quarkiverse.zanzibar.jaxrs;

import static javax.ws.rs.Priorities.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.time.Duration;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

@ApplicationScoped
@Provider
@Priority(AUTHORIZATION)
public class ZanzibarSynchronousAuthorizationFilter extends ZanzibarAuthorizationFilter implements ContainerRequestFilter {

    private static final Logger log = Logger.getLogger(ZanzibarSynchronousAuthorizationFilter.class);

    @Override
    public void filter(ContainerRequestContext context) {

        var checkResult = prepare(context);

        if (checkResult instanceof Result.NoCheck) {

            // No check needed

        } else if (checkResult instanceof Result.Forbidden) {

            context.abortWith(Response.status(FORBIDDEN).build());

        } else if (checkResult instanceof Result.Check) {

            var check = (Result.Check) checkResult;

            try {

                var allowed = authorizer.check(check.type, check.object, check.relation, check.user)
                        .await().atMost(Duration.ofSeconds(10));

                if (!allowed) {
                    context.abortWith(Response.status(FORBIDDEN).build());
                }

            } catch (Throwable t) {

                log.error("Authorization check failed", t);

                context.abortWith(Response.status(INTERNAL_SERVER_ERROR).build());

            }

        } else {

            throw new IllegalStateException("Unsupported authorization result");

        }
    }
}
