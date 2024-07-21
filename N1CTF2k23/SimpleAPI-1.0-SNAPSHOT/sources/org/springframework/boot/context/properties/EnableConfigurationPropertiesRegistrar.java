package org.springframework.boot.context.properties;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/EnableConfigurationPropertiesRegistrar.class */
class EnableConfigurationPropertiesRegistrar implements ImportBeanDefinitionRegistrar {
    EnableConfigurationPropertiesRegistrar() {
    }

    @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerInfrastructureBeans(registry);
        ConfigurationPropertiesBeanRegistrar beanRegistrar = new ConfigurationPropertiesBeanRegistrar(registry);
        Set<Class<?>> types = getTypes(metadata);
        beanRegistrar.getClass();
        types.forEach(this::register);
    }

    private Set<Class<?>> getTypes(AnnotationMetadata metadata) {
        return (Set) metadata.getAnnotations().stream(EnableConfigurationProperties.class).flatMap(annotation -> {
            return Arrays.stream(annotation.getClassArray("value"));
        }).filter(type -> {
            return Void.TYPE != type;
        }).collect(Collectors.toSet());
    }

    static void registerInfrastructureBeans(BeanDefinitionRegistry registry) {
        ConfigurationPropertiesBindingPostProcessor.register(registry);
        BoundConfigurationProperties.register(registry);
        ConfigurationPropertiesBeanDefinitionValidator.register(registry);
        ConfigurationBeanFactoryMetadata.register(registry);
    }
}
