package io.quarkiverse.zanzibar.jaxrs;

import static io.quarkiverse.zanzibar.jaxrs.annotations.FGADynamicObject.Source.PATH;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkiverse.zanzibar.RelationshipManager;
import io.quarkiverse.zanzibar.jaxrs.annotations.*;

public class ZanzibarAuthorizationFilter {

    public static final Logger log = Logger.getLogger(ZanzibarAuthorizationFilter.class);

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

            @Override
            public String toString() {
                return "Check{" +
                        "type='" + type + '\'' +
                        ", object='" + object + '\'' +
                        ", relation='" + relation + '\'' +
                        ", user='" + user + '\'' +
                        '}';
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

    static class AuthorizationAnnotations {
        Optional<FGARelation> relationAllowed;
        Optional<FGADynamicObject> dynamicObject;
        Optional<FGAObject> constantObject;

        public AuthorizationAnnotations(Optional<FGARelation> relationAllowed, Optional<FGADynamicObject> dynamicObject,
                Optional<FGAObject> constantObject) {
            this.relationAllowed = relationAllowed;
            this.dynamicObject = dynamicObject;
            this.constantObject = constantObject;
        }
    }

    Map<Method, AuthorizationAnnotations> authorizationAnnotationsCache = new ConcurrentHashMap<>();

    @Inject
    RelationshipManager relationshipManager;

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
            String message = FGARelation.class.getSimpleName() + " not found for method "
                    + resourceInfo.getResourceMethod().toGenericString();
            throw new IllegalStateException(message);
        }
        var relationAllowed = authorizationAnnotations.relationAllowed.get();
        var relation = relationAllowed.value();

        // Check for public/any access
        if (relation.equals(FGARelation.ANY)) {
            log.debug("Skipping authorization check, any relation allowed");
            return Result.noCheck();
        }

        // Determine object

        String objectType;
        String objectId;
        if (authorizationAnnotations.dynamicObject.isPresent()) {

            var dynamicObject = authorizationAnnotations.dynamicObject.get();

            objectType = dynamicObject.type();
            objectId = lookupObjectId(dynamicObject, context);

        } else if (authorizationAnnotations.constantObject.isPresent()) {

            var constantObject = authorizationAnnotations.constantObject.get();

            objectType = constantObject.type();
            objectId = constantObject.id();

        } else {
            String message = "No FGA object specifier found for method " + resourceInfo.getResourceMethod().toGenericString();
            throw new IllegalStateException(message);
        }

        // Determine user

        var principal = context.getSecurityContext().getUserPrincipal();

        String user;
        if (principal == null || principal.getName() == null) {

            // No principal... map to unauthenticated (if available)

            if (unauthenticatedUser.isEmpty()) {

                log.debug("No use principal or name and unauthenticated users are disallowed");

                return Result.forbidden();
            }

            log.debug("No use principal or name, authorizing the unauthenticated user");

            user = unauthenticatedUser.get();

        } else {

            user = principal.getName();
        }

        // Build check

        return Result.check(objectType, objectId, relation, user);
    }

    String lookupObjectId(FGADynamicObject query, ContainerRequestContext context) {
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

            var relationAllowedAnn = findAnnotation(resourceInfo, FGARelation.class);
            var constantObjectAnn = findAnnotation(resourceInfo, FGAObject.class);
            var dynamicObjectAnn = findAnnotation(resourceInfo, FGADynamicObject.class);

            if (dynamicObjectAnn.isEmpty()) {
                var pathObjectAnn = findAnnotation(resourceInfo, FGAPathObject.class);
                if (pathObjectAnn.isPresent()) {
                    dynamicObjectAnn = Optional
                            .of(new FGADynamicObject.Literal(PATH, pathObjectAnn.get().param(), pathObjectAnn.get().type()));
                }
            }
            if (dynamicObjectAnn.isEmpty()) {
                var queryObjectAnn = findAnnotation(resourceInfo, FGAQueryObject.class);
                if (queryObjectAnn.isPresent()) {
                    dynamicObjectAnn = Optional
                            .of(new FGADynamicObject.Literal(PATH, queryObjectAnn.get().param(), queryObjectAnn.get().type()));
                }
            }
            if (dynamicObjectAnn.isEmpty()) {
                var headerObjectAnn = findAnnotation(resourceInfo, FGAHeaderObject.class);
                if (headerObjectAnn.isPresent()) {
                    dynamicObjectAnn = Optional
                            .of(new FGADynamicObject.Literal(PATH, headerObjectAnn.get().name(), headerObjectAnn.get().type()));
                }
            }
            if (dynamicObjectAnn.isEmpty()) {
                var requestObjectAnn = findAnnotation(resourceInfo, FGARequestObject.class);
                if (requestObjectAnn.isPresent()) {
                    dynamicObjectAnn = Optional.of(new FGADynamicObject.Literal(PATH, requestObjectAnn.get().property(),
                            requestObjectAnn.get().type()));
                }
            }

            return new AuthorizationAnnotations(relationAllowedAnn, dynamicObjectAnn, constantObjectAnn);
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
