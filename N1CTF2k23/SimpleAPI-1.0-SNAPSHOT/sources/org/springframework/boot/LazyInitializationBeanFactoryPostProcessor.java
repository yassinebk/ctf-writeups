package org.springframework.boot;

import java.util.Collection;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.Ordered;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/LazyInitializationBeanFactoryPostProcessor.class */
public final class LazyInitializationBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered {
    @Override // org.springframework.beans.factory.config.BeanFactoryPostProcessor
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames;
        Collection<LazyInitializationExcludeFilter> filters = beanFactory.getBeansOfType(LazyInitializationExcludeFilter.class, false, false).values();
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (beanDefinition instanceof AbstractBeanDefinition) {
                postProcess(beanFactory, filters, beanName, (AbstractBeanDefinition) beanDefinition);
            }
        }
    }

    private void postProcess(ConfigurableListableBeanFactory beanFactory, Collection<LazyInitializationExcludeFilter> filters, String beanName, AbstractBeanDefinition beanDefinition) {
        Boolean lazyInit = beanDefinition.getLazyInit();
        if (lazyInit != null) {
            return;
        }
        Class<?> beanType = getBeanType(beanFactory, beanName);
        if (!isExcluded(filters, beanName, beanDefinition, beanType)) {
            beanDefinition.setLazyInit(true);
        }
    }

    private Class<?> getBeanType(ConfigurableListableBeanFactory beanFactory, String beanName) {
        try {
            return beanFactory.getType(beanName, false);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    private boolean isExcluded(Collection<LazyInitializationExcludeFilter> filters, String beanName, AbstractBeanDefinition beanDefinition, Class<?> beanType) {
        if (beanType != null) {
            for (LazyInitializationExcludeFilter filter : filters) {
                if (filter.isExcluded(beanName, beanDefinition, beanType)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
