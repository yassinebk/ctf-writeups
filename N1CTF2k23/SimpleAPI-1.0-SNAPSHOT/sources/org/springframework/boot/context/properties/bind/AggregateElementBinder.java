package org.springframework.boot.context.properties.bind;

import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
/* JADX INFO: Access modifiers changed from: package-private */
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/AggregateElementBinder.class */
public interface AggregateElementBinder {
    Object bind(ConfigurationPropertyName name, Bindable<?> target, ConfigurationPropertySource source);

    default Object bind(ConfigurationPropertyName name, Bindable<?> target) {
        return bind(name, target, null);
    }
}
