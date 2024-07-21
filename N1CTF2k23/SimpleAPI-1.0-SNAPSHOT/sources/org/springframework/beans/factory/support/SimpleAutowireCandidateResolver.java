package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/SimpleAutowireCandidateResolver.class */
public class SimpleAutowireCandidateResolver implements AutowireCandidateResolver {
    @Override // org.springframework.beans.factory.support.AutowireCandidateResolver
    public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
        return bdHolder.getBeanDefinition().isAutowireCandidate();
    }

    @Override // org.springframework.beans.factory.support.AutowireCandidateResolver
    public boolean isRequired(DependencyDescriptor descriptor) {
        return descriptor.isRequired();
    }

    @Override // org.springframework.beans.factory.support.AutowireCandidateResolver
    @Nullable
    public Object getSuggestedValue(DependencyDescriptor descriptor) {
        return null;
    }

    @Override // org.springframework.beans.factory.support.AutowireCandidateResolver
    @Nullable
    public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, @Nullable String beanName) {
        return null;
    }
}
