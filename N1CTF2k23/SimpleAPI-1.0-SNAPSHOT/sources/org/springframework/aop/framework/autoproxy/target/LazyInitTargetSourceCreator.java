package org.springframework.aop.framework.autoproxy.target;

import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/autoproxy/target/LazyInitTargetSourceCreator.class */
public class LazyInitTargetSourceCreator extends AbstractBeanFactoryBasedTargetSourceCreator {
    @Override // org.springframework.aop.framework.autoproxy.target.AbstractBeanFactoryBasedTargetSourceCreator
    protected boolean isPrototypeBased() {
        return false;
    }

    @Override // org.springframework.aop.framework.autoproxy.target.AbstractBeanFactoryBasedTargetSourceCreator
    @Nullable
    protected AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> beanClass, String beanName) {
        if (getBeanFactory() instanceof ConfigurableListableBeanFactory) {
            BeanDefinition definition = ((ConfigurableListableBeanFactory) getBeanFactory()).getBeanDefinition(beanName);
            if (definition.isLazyInit()) {
                return new LazyInitTargetSource();
            }
            return null;
        }
        return null;
    }
}
