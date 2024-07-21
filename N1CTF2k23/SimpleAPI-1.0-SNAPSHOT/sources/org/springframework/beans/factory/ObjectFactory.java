package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/ObjectFactory.class */
public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
