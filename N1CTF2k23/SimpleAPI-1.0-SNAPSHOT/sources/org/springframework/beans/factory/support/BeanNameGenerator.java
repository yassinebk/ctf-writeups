package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/BeanNameGenerator.class */
public interface BeanNameGenerator {
    String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry);
}
