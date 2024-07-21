package org.springframework.boot.context.config;

import java.util.Set;
import java.util.function.Consumer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/config/FilteredPropertySource.class */
class FilteredPropertySource extends PropertySource<PropertySource<?>> {
    private final Set<String> filteredProperties;

    FilteredPropertySource(PropertySource<?> original, Set<String> filteredProperties) {
        super(original.getName(), original);
        this.filteredProperties = filteredProperties;
    }

    @Override // org.springframework.core.env.PropertySource
    public Object getProperty(String name) {
        if (this.filteredProperties.contains(name)) {
            return null;
        }
        return getSource().getProperty(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void apply(ConfigurableEnvironment environment, String propertySourceName, Set<String> filteredProperties, Consumer<PropertySource<?>> operation) {
        MutablePropertySources propertySources = environment.getPropertySources();
        PropertySource<?> original = propertySources.get(propertySourceName);
        if (original == null) {
            operation.accept(null);
            return;
        }
        propertySources.replace(propertySourceName, new FilteredPropertySource(original, filteredProperties));
        try {
            operation.accept(original);
            propertySources.replace(propertySourceName, original);
        } catch (Throwable th) {
            propertySources.replace(propertySourceName, original);
            throw th;
        }
    }
}
