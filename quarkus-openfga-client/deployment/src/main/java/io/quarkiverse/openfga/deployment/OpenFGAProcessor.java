package io.quarkiverse.openfga.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;

import javax.enterprise.context.ApplicationScoped;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.api.API;
import io.quarkiverse.openfga.client.model.Store;
import io.quarkiverse.openfga.runtime.config.OpenFGAConfig;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.SslNativeConfigBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.vertx.deployment.VertxBuildItem;

class OpenFGAProcessor {

    static final String FEATURE = "openfga-client";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void build(BuildProducer<ReflectiveClassBuildItem> reflectiveClasses,
            CombinedIndexBuildItem combinedIndexBuildItem,
            SslNativeConfigBuildItem sslNativeConfig,
            OpenFGABuildTimeConfig config,
            BuildProducer<ExtensionSslNativeSupportBuildItem> sslNativeSupport) {

        final String[] modelClasses = combinedIndexBuildItem.getIndex()
                .getKnownClasses().stream()
                .filter(c -> c.name().packagePrefix().startsWith(Store.class.getPackageName()))
                .map(c -> c.name().toString())
                .toArray(String[]::new);
        reflectiveClasses.produce(ReflectiveClassBuildItem.weakClass(modelClasses));

        sslNativeSupport.produce(new ExtensionSslNativeSupportBuildItem(FEATURE));
    }

    @BuildStep
    @Record(RUNTIME_INIT)
    void registerSyntheticBeans(OpenFGABuildTimeConfig buildTimeConfig, OpenFGAConfig runtimeConfig,
            OpenFGARecorder recorder, VertxBuildItem vertx,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeans) {

        var apiValue = recorder.createAPI(runtimeConfig.url, runtimeConfig.sharedKey, vertx.getVertx());

        syntheticBeans.produce(
                SyntheticBeanBuildItem.configure(API.class)
                        .scope(ApplicationScoped.class)
                        .runtimeValue(apiValue)
                        .setRuntimeInit()
                        .done());

        syntheticBeans.produce(
                SyntheticBeanBuildItem.configure(StoreClient.class)
                        .scope(ApplicationScoped.class)
                        .setRuntimeInit()
                        .runtimeValue(recorder.createStoreClient(apiValue, runtimeConfig.storeId))
                        .done());

        syntheticBeans.produce(
                SyntheticBeanBuildItem.configure(AuthorizationModelClient.class)
                        .scope(ApplicationScoped.class)
                        .setRuntimeInit()
                        .runtimeValue(recorder.createAuthorizationModelClient(apiValue, runtimeConfig.storeId,
                                runtimeConfig.authorizationModelId))
                        .done());
    }
}
