package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/ScopeMetadataResolver.class */
public interface ScopeMetadataResolver {
    ScopeMetadata resolveScopeMetadata(BeanDefinition beanDefinition);
}
