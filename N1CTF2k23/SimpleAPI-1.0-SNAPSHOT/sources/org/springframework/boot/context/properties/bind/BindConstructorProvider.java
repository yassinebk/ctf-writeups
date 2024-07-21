package org.springframework.boot.context.properties.bind;

import java.lang.reflect.Constructor;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/BindConstructorProvider.class */
public interface BindConstructorProvider {
    public static final BindConstructorProvider DEFAULT = new DefaultBindConstructorProvider();

    Constructor<?> getBindConstructor(Bindable<?> bindable, boolean isNestedConstructorBinding);
}
