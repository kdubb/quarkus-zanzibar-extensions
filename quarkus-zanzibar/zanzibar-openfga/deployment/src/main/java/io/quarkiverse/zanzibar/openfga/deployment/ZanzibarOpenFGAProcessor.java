package io.quarkiverse.zanzibar.openfga.deployment;

import io.quarkiverse.zanzibar.openfga.ZanzibarOpenFGAAuthorizer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class ZanzibarOpenFGAProcessor {

    private static final String FEATURE = "zanzibar-open-fga";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerAuthorizer(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(
                AdditionalBeanBuildItem.builder()
                        .addBeanClass(ZanzibarOpenFGAAuthorizer.class)
                        .build());
    }

}
