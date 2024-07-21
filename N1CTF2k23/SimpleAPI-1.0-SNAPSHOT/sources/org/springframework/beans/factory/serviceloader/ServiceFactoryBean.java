package org.springframework.beans.factory.serviceloader;

import java.util.Iterator;
import java.util.ServiceLoader;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/serviceloader/ServiceFactoryBean.class */
public class ServiceFactoryBean extends AbstractServiceLoaderBasedFactoryBean implements BeanClassLoaderAware {
    @Override // org.springframework.beans.factory.serviceloader.AbstractServiceLoaderBasedFactoryBean
    protected Object getObjectToExpose(ServiceLoader<?> serviceLoader) {
        Iterator<?> it = serviceLoader.iterator();
        if (!it.hasNext()) {
            throw new IllegalStateException("ServiceLoader could not find service for type [" + getServiceType() + "]");
        }
        return it.next();
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.FactoryBean
    @Nullable
    public Class<?> getObjectType() {
        return getServiceType();
    }
}
