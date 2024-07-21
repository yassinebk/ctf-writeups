package org.springframework.boot.context.properties;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/BoundConfigurationProperties.class */
public class BoundConfigurationProperties {
    private Map<ConfigurationPropertyName, ConfigurationProperty> properties = new LinkedHashMap();
    private static final String BEAN_NAME = BoundConfigurationProperties.class.getName();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(ConfigurationProperty configurationProperty) {
        this.properties.put(configurationProperty.getName(), configurationProperty);
    }

    public ConfigurationProperty get(ConfigurationPropertyName name) {
        return this.properties.get(name);
    }

    public Map<ConfigurationPropertyName, ConfigurationProperty> getAll() {
        return Collections.unmodifiableMap(this.properties);
    }

    public static BoundConfigurationProperties get(ApplicationContext context) {
        if (!context.containsBeanDefinition(BEAN_NAME)) {
            return null;
        }
        return (BoundConfigurationProperties) context.getBean(BEAN_NAME, BoundConfigurationProperties.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void register(BeanDefinitionRegistry registry) {
        Assert.notNull(registry, "Registry must not be null");
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(BoundConfigurationProperties.class);
            definition.setRole(2);
            registry.registerBeanDefinition(BEAN_NAME, definition);
        }
    }
}
