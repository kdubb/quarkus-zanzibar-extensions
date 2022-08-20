package io.quarkiverse.zanzibar.deployment;

import io.quarkiverse.zanzibar.jaxrs.ZanzibarReactiveAuthorizationFilter;
import io.quarkiverse.zanzibar.jaxrs.ZanzibarSynchronousAuthorizationFilter;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.Capability;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class ZanzibarProcessor {

    public static final String FEATURE = "zanzibar";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerProvider(ZanzibarConfig config, Capabilities capabilities,
            BuildProducer<AdditionalBeanBuildItem> additionalBeans,
            BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
            BuildProducer<AdditionalIndexedClassesBuildItem> additionalIndexedClasses) {

        if (!config.filter.enabled) {
            return;
        }

        Class<?> filterClass;

        if (capabilities.isPresent(Capability.RESTEASY_REACTIVE)) {
            filterClass = ZanzibarReactiveAuthorizationFilter.class;
        } else if (capabilities.isPresent(Capability.RESTEASY)) {
            filterClass = ZanzibarSynchronousAuthorizationFilter.class;
        } else {
            return;
        }

        additionalBeans.produce(AdditionalBeanBuildItem.unremovableOf(filterClass));
        additionalIndexedClasses.produce(new AdditionalIndexedClassesBuildItem(filterClass.getName()));
        reflectiveClass.produce(new ReflectiveClassBuildItem(true, true, filterClass));
    }

}
