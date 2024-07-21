package org.springframework.boot.context.properties.bind;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/DataObjectPropertyBinder.class */
interface DataObjectPropertyBinder {
    Object bindProperty(String propertyName, Bindable<?> target);
}
