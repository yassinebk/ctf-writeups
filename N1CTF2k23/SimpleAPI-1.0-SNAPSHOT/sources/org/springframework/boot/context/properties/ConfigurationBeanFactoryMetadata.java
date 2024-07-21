package org.springframework.boot.context.properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationBeanFactoryMetadata.class */
public class ConfigurationBeanFactoryMetadata implements ApplicationContextAware {
    public static final String BEAN_NAME = ConfigurationBeanFactoryMetadata.class.getName();
    private ConfigurableApplicationContext applicationContext;

    public <A extends Annotation> Map<String, Object> getBeansWithFactoryAnnotation(Class<A> type) {
        String[] beanDefinitionNames;
        Map<String, Object> result = new HashMap<>();
        for (String name : this.applicationContext.getBeanFactory().getBeanDefinitionNames()) {
            if (findFactoryAnnotation(name, type) != null) {
                result.put(name, this.applicationContext.getBean(name));
            }
        }
        return result;
    }

    public <A extends Annotation> A findFactoryAnnotation(String beanName, Class<A> type) {
        Method method = findFactoryMethod(beanName);
        if (method != null) {
            return (A) AnnotationUtils.findAnnotation(method, (Class<Annotation>) type);
        }
        return null;
    }

    public Method findFactoryMethod(String beanName) {
        ConfigurableListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();
        if (beanFactory.containsBeanDefinition(beanName)) {
            BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
            if (beanDefinition instanceof RootBeanDefinition) {
                return ((RootBeanDefinition) beanDefinition).getResolvedFactoryMethod();
            }
            return null;
        }
        return null;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void register(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(ConfigurationBeanFactoryMetadata.class);
            definition.setRole(2);
            registry.registerBeanDefinition(BEAN_NAME, definition);
        }
    }
}
