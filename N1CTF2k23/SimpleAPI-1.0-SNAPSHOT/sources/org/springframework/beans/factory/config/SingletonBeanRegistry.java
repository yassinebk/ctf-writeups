package org.springframework.beans.factory.config;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/config/SingletonBeanRegistry.class */
public interface SingletonBeanRegistry {
    void registerSingleton(String str, Object obj);

    @Nullable
    Object getSingleton(String str);

    boolean containsSingleton(String str);

    String[] getSingletonNames();

    int getSingletonCount();

    Object getSingletonMutex();
}
