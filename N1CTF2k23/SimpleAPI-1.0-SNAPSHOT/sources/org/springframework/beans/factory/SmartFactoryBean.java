package org.springframework.beans.factory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/SmartFactoryBean.class */
public interface SmartFactoryBean<T> extends FactoryBean<T> {
    default boolean isPrototype() {
        return false;
    }

    default boolean isEagerInit() {
        return false;
    }
}
