package org.springframework.remoting.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/support/RemotingSupport.class */
public abstract class RemotingSupport implements BeanClassLoaderAware {
    protected final Log logger = LogFactory.getLog(getClass());
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public ClassLoader overrideThreadContextClassLoader() {
        return ClassUtils.overrideThreadContextClassLoader(getBeanClassLoader());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void resetThreadContextClassLoader(@Nullable ClassLoader original) {
        if (original != null) {
            Thread.currentThread().setContextClassLoader(original);
        }
    }
}
