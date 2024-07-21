package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/BeanDefinitionReader.class */
public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    @Nullable
    ResourceLoader getResourceLoader();

    @Nullable
    ClassLoader getBeanClassLoader();

    BeanNameGenerator getBeanNameGenerator();

    int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(Resource... resourceArr) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(String str) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(String... strArr) throws BeanDefinitionStoreException;
}
