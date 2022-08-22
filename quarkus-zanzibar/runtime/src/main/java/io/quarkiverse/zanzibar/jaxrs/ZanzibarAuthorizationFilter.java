package io.quarkiverse.zanzibar.jaxrs;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkiverse.zanzibar.Authorizer;
import io.quarkiverse.zanzibar.jaxrs.annotations.RelationAllowed;
import io.quarkiverse.zanzibar.jaxrs.annotations.RelationshipObject;

public class ZanzibarAuthorizationFilter {

    public interface Result {

        final class Check implements Result {
            public final String type;
            public final String object;
            public final String relation;
            public final String user;

            public Check(String type, String object, String relation, String user) {
                this.type = type;
                this.object = object;
                this.relation = relation;
                this.user = user;
            }
        }

        static Check check(String type, String object, String relation, String user) {
            return new Check(type, object, relation, user);
        }

        final class NoCheck implements Result {
            public static NoCheck INSTANCE = new NoCheck();
        }

        static Result noCheck() {
            return NoCheck.INSTANCE;
        }

        final class Forbidden implements Result {
            public static Forbidden INSTANCE = new Forbidden();
        }

        static Forbidden forbidden() {
            return Forbidden.INSTANCE;
        }
    }

    class AuthorizationAnnotations {
        Optional<RelationAllowed> relationAllowed;
        Optional<RelationshipObject> objectQuery;

        public AuthorizationAnnotations(Optional<RelationAllowed> relationAllowed, Optional<RelationshipObject> objectQuery) {
            this.relationAllowed = relationAllowed;
            this.objectQuery = objectQuery;
        }
    }

    Map<Method, AuthorizationAnnotations> authorizationAnnotationsCache = new HashMap<>();

    @Inject
    Authorizer authorizer;

    @Context
    ResourceInfo resourceInfo;

    @ConfigProperty(name = "quarkus.open-fga.filter.unauthenticated-user")
    Optional<String> unauthenticatedUser;

    @ConfigProperty(name = "quarkus.open-fga.filter.timeout", defaultValue = "5s")
    Duration timeout;

    protected Result prepare(ContainerRequestContext context) {

        var authorizationAnnotations = getAuthorizationAnnotations(resourceInfo);

        // Determine relation

        if (authorizationAnnotations.relationAllowed.isEmpty()) {
            throw new IllegalStateException("RelationAllowed not found");
        }
        var relationAllowed = authorizationAnnotations.relationAllowed.get();
        var relation = relationAllowed.value();

        // Check for public/any access
        if (relation.equals(RelationAllowed.ANY)) {
            return Result.noCheck();
        }

        // Determine object

        if (authorizationAnnotations.objectQuery.isEmpty()) {
            throw new IllegalStateException("ObjectQuery not found");
        }
        var objectQuery = authorizationAnnotations.objectQuery.get();

        var object = queryObject(objectQuery, context);

        // Determine user

        var principal = context.getSecurityContext().getUserPrincipal();

        String user;
        if (principal == null || principal.getName() == null) {

            // No principal... map to unauthenticated (if available)

            if (unauthenticatedUser.isEmpty()) {
                return Result.forbidden();
            }

            user = unauthenticatedUser.get();

        } else {

            user = principal.getName();
        }

        // Build check

        return Result.check(objectQuery.type(), object, relation, user);
    }

    String queryObject(RelationshipObject query, ContainerRequestContext context) {
        var uriInfo = context.getUriInfo();

        String object;
        switch (query.source()) {
            case PATH:
                object = uriInfo.getPathParameters().getFirst(query.sourceProperty());
                break;
            case QUERY:
                object = uriInfo.getQueryParameters().getFirst(query.sourceProperty());
                break;
            case HEADER:
                object = context.getHeaderString(query.sourceProperty());
                break;
            case REQUEST:
                var value = context.getProperty(query.sourceProperty());
                if (value != null) {
                    object = value.toString();
                } else {
                    object = null;
                }
                break;
            default:
                throw new IllegalStateException("Invalid object source");
        }

        if (object == null) {
            throw new IllegalStateException("Invalid object from relationship specification");
        }

        return object;
    }

    AuthorizationAnnotations getAuthorizationAnnotations(ResourceInfo resourceInfo) {
        return authorizationAnnotationsCache.computeIfAbsent(resourceInfo.getResourceMethod(), key -> {

            var relationAllowedAnn = findAnnotation(resourceInfo, RelationAllowed.class);
            var objectQueryAnn = findAnnotation(resourceInfo, RelationshipObject.class);

            return new AuthorizationAnnotations(relationAllowedAnn, objectQueryAnn);
        });
    }

    static <A extends Annotation> Optional<A> findAnnotation(ResourceInfo resourceInfo, Class<A> annotationType) {

        return findAnnotation(resourceInfo.getResourceMethod(), annotationType)
                .or(() -> findAnnotation(resourceInfo.getResourceClass(), annotationType));
    }

    static <A extends Annotation> Optional<A> findAnnotation(Method method, Class<A> annotationType) {

        return ofNullable(
                method.getAnnotation(annotationType)).or(() -> {

                    var declClass = method.getDeclaringClass();

                    // Check superclass
                    try {
                        var superMethod = declClass.getSuperclass().getDeclaredMethod(method.getName(),
                                method.getParameterTypes());
                        var ann = findAnnotation(superMethod, annotationType);
                        if (ann.isPresent()) {
                            return ann;
                        }
                    } catch (NullPointerException | NoSuchMethodException e) {
                        // Ignore
                    }

                    // Check interfaces
                    for (var iface : declClass.getInterfaces()) {
                        try {
                            var ifaceMethod = iface.getDeclaredMethod(method.getName(), method.getParameterTypes());
                            var ann = findAnnotation(ifaceMethod, annotationType);
                            if (ann.isPresent()) {
                                return ann;
                            }
                        } catch (NoSuchMethodException e) {
                            // Ignore
                        }
                    }

                    return empty();
                });
    }

    static <A extends Annotation> Optional<A> findAnnotation(Class<?> cls, Class<A> annotationType) {

        return ofNullable(

                // Inheritance handled via @Inherited if present
                cls.getAnnotation(annotationType)

        ).or(() -> {

            // Check interfaces
            for (var iface : cls.getInterfaces()) {
                var ann = findAnnotation(iface, annotationType);
                if (ann.isPresent()) {
                    return ann;
                }
            }

            return empty();
        });
    }

}
