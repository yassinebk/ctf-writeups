package org.springframework.boot.autoconfigure;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/AutoConfigurationExcludeFilter.class */
public class AutoConfigurationExcludeFilter implements TypeFilter, BeanClassLoaderAware {
    private ClassLoader beanClassLoader;
    private volatile List<String> autoConfigurations;

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override // org.springframework.core.type.filter.TypeFilter
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return isConfiguration(metadataReader) && isAutoConfiguration(metadataReader);
    }

    private boolean isConfiguration(MetadataReader metadataReader) {
        return metadataReader.getAnnotationMetadata().isAnnotated(Configuration.class.getName());
    }

    private boolean isAutoConfiguration(MetadataReader metadataReader) {
        return getAutoConfigurations().contains(metadataReader.getClassMetadata().getClassName());
    }

    protected List<String> getAutoConfigurations() {
        if (this.autoConfigurations == null) {
            this.autoConfigurations = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, this.beanClassLoader);
        }
        return this.autoConfigurations;
    }
}
