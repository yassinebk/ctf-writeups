package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanPostProcessor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/MergedBeanDefinitionPostProcessor.class */
public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {
    void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> cls, String str);

    default void resetBeanDefinition(String beanName) {
    }
}
