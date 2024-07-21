package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/annotation/AnnotatedBeanDefinition.class */
public interface AnnotatedBeanDefinition extends BeanDefinition {
    AnnotationMetadata getMetadata();

    @Nullable
    MethodMetadata getFactoryMethodMetadata();
}
