package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/AutowireCandidateResolver.class */
public interface AutowireCandidateResolver {
    default boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
        return bdHolder.getBeanDefinition().isAutowireCandidate();
    }

    default boolean isRequired(DependencyDescriptor descriptor) {
        return descriptor.isRequired();
    }

    default boolean hasQualifier(DependencyDescriptor descriptor) {
        return false;
    }

    @Nullable
    default Object getSuggestedValue(DependencyDescriptor descriptor) {
        return null;
    }

    @Nullable
    default Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, @Nullable String beanName) {
        return null;
    }
}
