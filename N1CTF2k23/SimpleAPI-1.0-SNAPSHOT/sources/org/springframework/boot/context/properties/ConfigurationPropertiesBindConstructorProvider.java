package org.springframework.boot.context.properties;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.bind.BindConstructorProvider;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.core.KotlinDetector;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBindConstructorProvider.class */
class ConfigurationPropertiesBindConstructorProvider implements BindConstructorProvider {
    static final ConfigurationPropertiesBindConstructorProvider INSTANCE = new ConfigurationPropertiesBindConstructorProvider();

    ConfigurationPropertiesBindConstructorProvider() {
    }

    @Override // org.springframework.boot.context.properties.bind.BindConstructorProvider
    public Constructor<?> getBindConstructor(Bindable<?> bindable, boolean isNestedConstructorBinding) {
        return getBindConstructor(bindable.getType().resolve(), isNestedConstructorBinding);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Constructor<?> getBindConstructor(Class<?> type, boolean isNestedConstructorBinding) {
        if (type == null) {
            return null;
        }
        Constructor<?> constructor = findConstructorBindingAnnotatedConstructor(type);
        if (constructor == null && (isConstructorBindingAnnotatedType(type) || isNestedConstructorBinding)) {
            constructor = deduceBindConstructor(type);
        }
        return constructor;
    }

    private Constructor<?> findConstructorBindingAnnotatedConstructor(Class<?> type) {
        Constructor<?> constructor;
        if (isKotlinType(type) && (constructor = BeanUtils.findPrimaryConstructor(type)) != null) {
            return findAnnotatedConstructor(type, constructor);
        }
        return findAnnotatedConstructor(type, type.getDeclaredConstructors());
    }

    private Constructor<?> findAnnotatedConstructor(Class<?> type, Constructor<?>... candidates) {
        Constructor<?> constructor = null;
        for (Constructor<?> candidate : candidates) {
            if (MergedAnnotations.from(candidate).isPresent(ConstructorBinding.class)) {
                Assert.state(candidate.getParameterCount() > 0, type.getName() + " declares @ConstructorBinding on a no-args constructor");
                Assert.state(constructor == null, type.getName() + " has more than one @ConstructorBinding constructor");
                constructor = candidate;
            }
        }
        return constructor;
    }

    private boolean isConstructorBindingAnnotatedType(Class<?> type) {
        return MergedAnnotations.from(type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY_AND_ENCLOSING_CLASSES).isPresent(ConstructorBinding.class);
    }

    private Constructor<?> deduceBindConstructor(Class<?> type) {
        if (isKotlinType(type)) {
            return deducedKotlinBindConstructor(type);
        }
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 1 && constructors[0].getParameterCount() > 0) {
            return constructors[0];
        }
        return null;
    }

    private Constructor<?> deducedKotlinBindConstructor(Class<?> type) {
        Constructor<?> primaryConstructor = BeanUtils.findPrimaryConstructor(type);
        if (primaryConstructor != null && primaryConstructor.getParameterCount() > 0) {
            return primaryConstructor;
        }
        return null;
    }

    private boolean isKotlinType(Class<?> type) {
        return KotlinDetector.isKotlinPresent() && KotlinDetector.isKotlinType(type);
    }
}
