package io.quarkiverse.zanzibar.deployment;

import java.time.Duration;
import java.util.Optional;

import io.quarkiverse.zanzibar.jaxrs.annotations.RelationAllowed;
import io.quarkiverse.zanzibar.jaxrs.annotations.RelationshipObject;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

/**
 * Configuration for JAX-RS authorization filter.
 */
@ConfigGroup
public class JAXRSFilterConfig {

    /**
     * Whether the filter is enabled.
     * <p>
     * When enabled all endpoints must have a resolvable {@link RelationshipObject} and
     * {@link RelationAllowed} otherwise a FORBIDDEN will be returns to clients.
     */
    @ConfigItem(defaultValue = "true")
    public boolean enabled;

    /**
     * Name used for authorization when the request is unauthenticated.
     */
    @ConfigItem
    public Optional<String> unauthenticatedUser;

    /**
     * Maximum time an authorization check is allowed to take.
     */
    @ConfigItem(defaultValue = "5s")
    public Duration timeout;

}
