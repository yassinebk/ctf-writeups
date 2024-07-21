package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/InstantiationStrategy.class */
public interface InstantiationStrategy {
    Object instantiate(RootBeanDefinition rootBeanDefinition, @Nullable String str, BeanFactory beanFactory) throws BeansException;

    Object instantiate(RootBeanDefinition rootBeanDefinition, @Nullable String str, BeanFactory beanFactory, Constructor<?> constructor, Object... objArr) throws BeansException;

    Object instantiate(RootBeanDefinition rootBeanDefinition, @Nullable String str, BeanFactory beanFactory, @Nullable Object obj, Method method, Object... objArr) throws BeansException;
}
