package org.springframework.boot.context.properties.bind;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/DataObjectBinder.class */
public interface DataObjectBinder {
    <T> T bind(ConfigurationPropertyName name, Bindable<T> target, Binder.Context context, DataObjectPropertyBinder propertyBinder);

    <T> T create(Bindable<T> target, Binder.Context context);
}
