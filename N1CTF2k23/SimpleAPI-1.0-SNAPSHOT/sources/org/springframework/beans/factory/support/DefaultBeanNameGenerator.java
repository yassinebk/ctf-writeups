package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/DefaultBeanNameGenerator.class */
public class DefaultBeanNameGenerator implements BeanNameGenerator {
    public static final DefaultBeanNameGenerator INSTANCE = new DefaultBeanNameGenerator();

    @Override // org.springframework.beans.factory.support.BeanNameGenerator
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
    }
}
