package org.springframework.boot.context.properties.source;

import java.util.List;
import java.util.function.BiPredicate;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/source/PropertyMapper.class */
interface PropertyMapper {
    public static final BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> DEFAULT_ANCESTOR_OF_CHECK = (v0, v1) -> {
        return v0.isAncestorOf(v1);
    };

    List<String> map(ConfigurationPropertyName configurationPropertyName);

    ConfigurationPropertyName map(String propertySourceName);

    default BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> getAncestorOfCheck() {
        return DEFAULT_ANCESTOR_OF_CHECK;
    }
}
