package io.quarkiverse.openfga.deployment;

import java.net.URL;
import java.util.Optional;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.api.API;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import io.vertx.mutiny.core.Vertx;

@Recorder
public class OpenFGARecorder {

    static final Logger log = Logger.getLogger(OpenFGARecorder.class);

    public RuntimeValue<API> createAPI(URL url, Optional<String> sharedKey, RuntimeValue<io.vertx.core.Vertx> vertx) {
        log.infof("OpenFGA Config: url=%s, sharedKey=%s", url, sharedKey);
        // TODO Get mapper from Arc/CDI
        var objectMapper = new ObjectMapper();
        return new RuntimeValue<>(new API(url, sharedKey, Vertx.newInstance(vertx.getValue()), objectMapper));
    }

    public RuntimeValue<StoreClient> createStoreClient(RuntimeValue<API> api, String storeId) {
        log.infof("OpenFGA Config: storeId=%s", storeId);
        var storeClient = new StoreClient(api.getValue(), storeId);
        return new RuntimeValue<>(storeClient);
    }

    public RuntimeValue<AuthorizationModelClient> createAuthorizationModelClient(RuntimeValue<API> api, String storeId,
            Optional<String> authorizationModelId) {
        log.infof("OpenFGA Config: storeId=%s, authorizationModelId=%s", storeId, authorizationModelId);
        var authModelClient = new AuthorizationModelClient(api.getValue(), storeId, authorizationModelId.orElse(null));
        return new RuntimeValue<>(authModelClient);
    }

}
