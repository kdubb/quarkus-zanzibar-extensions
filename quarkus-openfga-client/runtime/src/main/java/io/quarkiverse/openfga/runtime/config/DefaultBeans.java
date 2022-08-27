package io.quarkiverse.openfga.runtime.config;

public class DefaultBeans {

    //    @Produces
    //    @ApplicationScoped
    //    public static StoresClient defaultStoresBean(API api) {
    //        return new StoresClient(api);
    //    }
    //
    //    @Produces
    //    @ApplicationScoped
    //    public static StoreClient defaultStoreBean(API api,
    //            @ConfigProperty(name = "quarkus.openfga.store-id") Optional<String> storeId) {
    //        return new StoreClient(api, storeId.orElseThrow(
    //                () -> new ConfigurationException("StoreClient requires a store-id", Set.of("quarkus.openfga.store-id"))));
    //    }
    //
    //    @Produces
    //    @ApplicationScoped
    //    public static AuthorizationModelClient defaultAutModelBean(API api,
    //            @ConfigProperty(name = "quarkus.openfga.store-id") Optional<String> storeId,
    //            @ConfigProperty(name = "quarkus.openfga.authorization-model-id") Optional<String> authorizationModelId) {
    //        return new AuthorizationModelClient(api,
    //                storeId.orElseThrow(() -> new ConfigurationException("AuthorizationModelClient requires a store-id",
    //                        Set.of("quarkus.openfga.store-id"))),
    //                authorizationModelId.orElseThrow(
    //                        () -> new ConfigurationException("AuthorizationModelClient requires an authorization-model-id",
    //                                Set.of("quarkus.openfga.authorization-model-id"))));
    //    }

}
