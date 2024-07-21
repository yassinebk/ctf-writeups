package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/config/BeanExpressionResolver.class */
public interface BeanExpressionResolver {
    @Nullable
    Object evaluate(@Nullable String str, BeanExpressionContext beanExpressionContext) throws BeansException;
}
