package org.springframework.boot.context.properties.source;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/source/CachingConfigurationPropertySource.class */
interface CachingConfigurationPropertySource {
    ConfigurationPropertyCaching getCaching();

    static ConfigurationPropertyCaching find(ConfigurationPropertySource source) {
        if (source instanceof CachingConfigurationPropertySource) {
            return ((CachingConfigurationPropertySource) source).getCaching();
        }
        return null;
    }
}
