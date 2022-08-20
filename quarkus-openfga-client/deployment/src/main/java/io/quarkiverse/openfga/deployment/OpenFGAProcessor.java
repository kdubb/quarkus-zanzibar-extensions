package io.quarkiverse.openfga.deployment;

import org.jboss.logging.Logger;

import io.quarkiverse.openfga.client.api.API;
import io.quarkiverse.openfga.client.model.Store;
import io.quarkiverse.openfga.runtime.config.DefaultBeans;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.SslNativeConfigBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class OpenFGAProcessor {

    static final String FEATURE = "open-fga";

    private static final Logger log = Logger.getLogger(OpenFGAProcessor.class);

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
    void registerAdditionalBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(
                AdditionalBeanBuildItem.builder()
                        .addBeanClass(API.class)
                        .addBeanClass(DefaultBeans.class)
                        .setRemovable()
                        .build());
    }

}
