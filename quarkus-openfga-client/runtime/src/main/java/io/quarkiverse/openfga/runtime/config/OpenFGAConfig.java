package io.quarkiverse.openfga.runtime.config;

import static io.quarkiverse.openfga.runtime.config.OpenFGAConfig.NAME;

import java.net.URL;
import java.util.Optional;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = NAME, phase = ConfigPhase.RUN_TIME)
public class OpenFGAConfig {

    public static final String NAME = "openfga";

    /**
     * OpenFGA server URL.
     * <p>
     * Example: http://openfga:8080
     */
    @ConfigItem
    public URL url;

    /**
     * Shared authentication key.
     */
    @ConfigItem
    public Optional<String> sharedKey;

    /**
     * TLS configuration.
     */
    @ConfigItem
    @ConfigDocSection
    public TLSConfig tls;

    /**
     * Store id for default {@link StoreClient} bean.
     */
    @ConfigItem
    public String storeId;

    /**
     * Authorization model id for default {@link AuthorizationModelClient} bean.
     */
    @ConfigItem
    public Optional<String> authorizationModelId;

    @Override
    public String toString() {
        return "OpenFGAConfig{" +
                "url=" + url +
                ", sharedKey=" + sharedKey +
                ", tls=" + tls +
                ", storeId=" + storeId +
                ", authorizationModelId=" + authorizationModelId +
                '}';
    }
}
