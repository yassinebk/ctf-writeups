package org.springframework.context.annotation;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/ScopedProxyCreator.class */
public final class ScopedProxyCreator {
    private ScopedProxyCreator() {
    }

    public static BeanDefinitionHolder createScopedProxy(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry, boolean proxyTargetClass) {
        return ScopedProxyUtils.createScopedProxy(definitionHolder, registry, proxyTargetClass);
    }

    public static String getTargetBeanName(String originalBeanName) {
        return ScopedProxyUtils.getTargetBeanName(originalBeanName);
    }
}
