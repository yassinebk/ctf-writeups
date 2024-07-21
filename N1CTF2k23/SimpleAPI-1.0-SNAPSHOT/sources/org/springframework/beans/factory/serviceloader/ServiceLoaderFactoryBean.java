package org.springframework.beans.factory.serviceloader;

import java.util.ServiceLoader;
import org.springframework.beans.factory.BeanClassLoaderAware;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/serviceloader/ServiceLoaderFactoryBean.class */
public class ServiceLoaderFactoryBean extends AbstractServiceLoaderBasedFactoryBean implements BeanClassLoaderAware {
    @Override // org.springframework.beans.factory.serviceloader.AbstractServiceLoaderBasedFactoryBean
    protected Object getObjectToExpose(ServiceLoader<?> serviceLoader) {
        return serviceLoader;
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return ServiceLoader.class;
    }
}
