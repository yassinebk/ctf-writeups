package org.springframework.boot.context.properties;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.ConfigurationPropertiesBean;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBeanRegistrar.class */
final class ConfigurationPropertiesBeanRegistrar {
    private final BeanDefinitionRegistry registry;
    private final BeanFactory beanFactory;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurationPropertiesBeanRegistrar(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.beanFactory = (BeanFactory) this.registry;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void register(Class<?> type) {
        MergedAnnotation<ConfigurationProperties> annotation = MergedAnnotations.from(type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ConfigurationProperties.class);
        register(type, annotation);
    }

    void register(Class<?> type, MergedAnnotation<ConfigurationProperties> annotation) {
        String name = getName(type, annotation);
        if (!containsBeanDefinition(name)) {
            registerBeanDefinition(name, type, annotation);
        }
    }

    private String getName(Class<?> type, MergedAnnotation<ConfigurationProperties> annotation) {
        String prefix = annotation.isPresent() ? annotation.getString("prefix") : "";
        return StringUtils.hasText(prefix) ? prefix + "-" + type.getName() : type.getName();
    }

    private boolean containsBeanDefinition(String name) {
        return containsBeanDefinition(this.beanFactory, name);
    }

    private boolean containsBeanDefinition(BeanFactory beanFactory, String name) {
        if ((beanFactory instanceof ListableBeanFactory) && ((ListableBeanFactory) beanFactory).containsBeanDefinition(name)) {
            return true;
        }
        if (beanFactory instanceof HierarchicalBeanFactory) {
            return containsBeanDefinition(((HierarchicalBeanFactory) beanFactory).getParentBeanFactory(), name);
        }
        return false;
    }

    private void registerBeanDefinition(String beanName, Class<?> type, MergedAnnotation<ConfigurationProperties> annotation) {
        Assert.state(annotation.isPresent(), () -> {
            return "No " + ConfigurationProperties.class.getSimpleName() + " annotation found on  '" + type.getName() + "'.";
        });
        this.registry.registerBeanDefinition(beanName, createBeanDefinition(beanName, type));
    }

    private BeanDefinition createBeanDefinition(String beanName, Class<?> type) {
        if (ConfigurationPropertiesBean.BindMethod.forType(type) == ConfigurationPropertiesBean.BindMethod.VALUE_OBJECT) {
            return new ConfigurationPropertiesValueObjectBeanDefinition(this.beanFactory, beanName, type);
        }
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(type);
        return definition;
    }
}
