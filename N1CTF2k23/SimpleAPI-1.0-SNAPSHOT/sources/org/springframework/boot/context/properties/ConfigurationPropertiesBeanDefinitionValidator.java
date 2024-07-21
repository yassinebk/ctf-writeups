package org.springframework.boot.context.properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.ConfigurationPropertiesBean;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBeanDefinitionValidator.class */
class ConfigurationPropertiesBeanDefinitionValidator implements BeanFactoryPostProcessor, Ordered {
    private static final String BEAN_NAME = ConfigurationPropertiesBeanDefinitionValidator.class.getName();

    ConfigurationPropertiesBeanDefinitionValidator() {
    }

    @Override // org.springframework.beans.factory.config.BeanFactoryPostProcessor
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames;
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            if (!beanFactory.containsSingleton(beanName) && !isValueObjectBeanDefinition(beanFactory, beanName)) {
                validate(beanFactory, beanName);
            }
        }
    }

    private boolean isValueObjectBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName) {
        BeanDefinition definition = beanFactory.getBeanDefinition(beanName);
        return definition instanceof ConfigurationPropertiesValueObjectBeanDefinition;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    private void validate(ConfigurableListableBeanFactory beanFactory, String beanName) {
        try {
            Class<?> beanClass = beanFactory.getType(beanName, false);
            if (beanClass != null && ConfigurationPropertiesBean.BindMethod.forType(beanClass) == ConfigurationPropertiesBean.BindMethod.VALUE_OBJECT) {
                throw new BeanCreationException(beanName, "@EnableConfigurationProperties or @ConfigurationPropertiesScan must be used to add @ConstructorBinding type " + beanClass.getName());
            }
        } catch (CannotLoadBeanClassException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void register(BeanDefinitionRegistry registry) {
        Assert.notNull(registry, "Registry must not be null");
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(ConfigurationPropertiesBeanDefinitionValidator.class);
            definition.setRole(2);
            registry.registerBeanDefinition(BEAN_NAME, definition);
        }
        ConfigurationPropertiesBinder.register(registry);
    }
}
