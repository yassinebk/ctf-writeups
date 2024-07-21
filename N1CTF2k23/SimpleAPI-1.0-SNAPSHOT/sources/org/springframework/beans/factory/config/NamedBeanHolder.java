package org.springframework.beans.factory.config;

import org.springframework.beans.factory.NamedBean;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/config/NamedBeanHolder.class */
public class NamedBeanHolder<T> implements NamedBean {
    private final String beanName;
    private final T beanInstance;

    public NamedBeanHolder(String beanName, T beanInstance) {
        Assert.notNull(beanName, "Bean name must not be null");
        this.beanName = beanName;
        this.beanInstance = beanInstance;
    }

    @Override // org.springframework.beans.factory.NamedBean
    public String getBeanName() {
        return this.beanName;
    }

    public T getBeanInstance() {
        return this.beanInstance;
    }
}
