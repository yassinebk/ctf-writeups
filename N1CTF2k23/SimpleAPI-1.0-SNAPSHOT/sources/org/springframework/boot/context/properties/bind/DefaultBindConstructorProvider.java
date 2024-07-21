package org.springframework.boot.context.properties.bind;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.KotlinDetector;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/DefaultBindConstructorProvider.class */
class DefaultBindConstructorProvider implements BindConstructorProvider {
    @Override // org.springframework.boot.context.properties.bind.BindConstructorProvider
    public Constructor<?> getBindConstructor(Bindable<?> bindable, boolean isNestedConstructorBinding) {
        Class<?> type = bindable.getType().resolve();
        if (bindable.getValue() != null || type == null) {
            return null;
        }
        if (KotlinDetector.isKotlinPresent() && KotlinDetector.isKotlinType(type)) {
            return getDeducedKotlinConstructor(type);
        }
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 1 && constructors[0].getParameterCount() > 0) {
            return constructors[0];
        }
        return null;
    }

    private Constructor<?> getDeducedKotlinConstructor(Class<?> type) {
        Constructor<?> primaryConstructor = BeanUtils.findPrimaryConstructor(type);
        if (primaryConstructor != null && primaryConstructor.getParameterCount() > 0) {
            return primaryConstructor;
        }
        return null;
    }
}
