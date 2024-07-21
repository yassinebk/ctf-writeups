package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/FullyQualifiedAnnotationBeanNameGenerator.class */
public class FullyQualifiedAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {
    @Override // org.springframework.context.annotation.AnnotationBeanNameGenerator
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No bean class name set");
        return beanClassName;
    }
}
