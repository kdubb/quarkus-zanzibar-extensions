package io.quarkiverse.openfga.deployment;

import io.quarkiverse.openfga.runtime.config.OpenFGAConfig;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = OpenFGAConfig.NAME, phase = ConfigPhase.BUILD_TIME)
public class OpenFGABuildTimeConfig {

    /**
     * Whether a health check is published in case the smallrye-health extension is present.
     */
    @ConfigItem(name = "health.enabled", defaultValue = "true")
    public boolean healthEnabled;

    /**
     * Whether metrics are published in case a metrics extension is present.
     */
    @ConfigItem(name = "metrics.enabled")
    public boolean metricsEnabled;

    /**
     * Whether tracing spans of driver commands are sent in case the smallrye-opentracing extension is present.
     */
    @ConfigItem(name = "tracing.enabled")
    public boolean tracingEnabled;

    /**
     * Dev services configuration.
     */
    @ConfigItem
    public DevServicesOpenFGAConfig devservices;

    @Override
    public String toString() {
        return "OpenFGABuildTimeConfig{" +
                "healthEnabled=" + healthEnabled +
                ", metricsEnabled=" + metricsEnabled +
                ", tracingEnabled=" + tracingEnabled +
                ", devservices=" + devservices +
                '}';
    }
}
