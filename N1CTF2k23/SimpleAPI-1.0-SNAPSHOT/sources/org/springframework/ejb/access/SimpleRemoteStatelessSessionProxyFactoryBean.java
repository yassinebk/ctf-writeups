package org.springframework.ejb.access;

import javax.naming.NamingException;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/ejb/access/SimpleRemoteStatelessSessionProxyFactoryBean.class */
public class SimpleRemoteStatelessSessionProxyFactoryBean extends SimpleRemoteSlsbInvokerInterceptor implements FactoryBean<Object>, BeanClassLoaderAware {
    @Nullable
    private Class<?> businessInterface;
    @Nullable
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    @Nullable
    private Object proxy;

    public void setBusinessInterface(@Nullable Class<?> businessInterface) {
        this.businessInterface = businessInterface;
    }

    @Nullable
    public Class<?> getBusinessInterface() {
        return this.businessInterface;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override // org.springframework.ejb.access.AbstractSlsbInvokerInterceptor, org.springframework.jndi.JndiObjectLocator, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NamingException {
        super.afterPropertiesSet();
        if (this.businessInterface == null) {
            throw new IllegalArgumentException("businessInterface is required");
        }
        this.proxy = new ProxyFactory(this.businessInterface, this).getProxy(this.beanClassLoader);
    }

    @Override // org.springframework.beans.factory.FactoryBean
    @Nullable
    public Object getObject() {
        return this.proxy;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return this.businessInterface;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
