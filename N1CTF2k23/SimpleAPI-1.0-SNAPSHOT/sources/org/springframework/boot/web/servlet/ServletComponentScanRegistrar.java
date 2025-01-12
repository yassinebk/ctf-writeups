package org.springframework.boot.web.servlet;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/ServletComponentScanRegistrar.class */
class ServletComponentScanRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String BEAN_NAME = "servletComponentRegisteringPostProcessor";

    ServletComponentScanRegistrar() {
    }

    @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);
        if (registry.containsBeanDefinition(BEAN_NAME)) {
            updatePostProcessor(registry, packagesToScan);
        } else {
            addPostProcessor(registry, packagesToScan);
        }
    }

    private void updatePostProcessor(BeanDefinitionRegistry registry, Set<String> packagesToScan) {
        BeanDefinition definition = registry.getBeanDefinition(BEAN_NAME);
        ConstructorArgumentValues.ValueHolder constructorArguments = definition.getConstructorArgumentValues().getGenericArgumentValue(Set.class);
        Set<String> mergedPackages = (Set) constructorArguments.getValue();
        mergedPackages.addAll(packagesToScan);
        constructorArguments.setValue(mergedPackages);
    }

    private void addPostProcessor(BeanDefinitionRegistry registry, Set<String> packagesToScan) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ServletComponentRegisteringPostProcessor.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(packagesToScan);
        beanDefinition.setRole(2);
        registry.registerBeanDefinition(BEAN_NAME, beanDefinition);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(ServletComponentScan.class.getName()));
        String[] basePackages = attributes.getStringArray("basePackages");
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        Set<String> packagesToScan = new LinkedHashSet<>(Arrays.asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        if (packagesToScan.isEmpty()) {
            packagesToScan.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packagesToScan;
    }
}
