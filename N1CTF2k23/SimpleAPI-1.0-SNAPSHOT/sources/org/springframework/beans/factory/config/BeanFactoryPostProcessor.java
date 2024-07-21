package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/config/BeanFactoryPostProcessor.class */
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException;
}
