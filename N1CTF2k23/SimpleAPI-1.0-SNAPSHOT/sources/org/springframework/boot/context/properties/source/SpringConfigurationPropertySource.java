package org.springframework.boot.context.properties.source;

import java.util.Map;
import java.util.Random;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.boot.origin.Origin;
import org.springframework.boot.origin.PropertySourceOrigin;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.util.Assert;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/source/SpringConfigurationPropertySource.class */
public class SpringConfigurationPropertySource implements ConfigurationPropertySource {
    private static final ConfigurationPropertyName RANDOM = ConfigurationPropertyName.of(RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME);
    private static final PropertyMapper[] DEFAULT_MAPPERS = {DefaultPropertyMapper.INSTANCE};
    private static final PropertyMapper[] SYSTEM_ENVIRONMENT_MAPPERS = {SystemEnvironmentPropertyMapper.INSTANCE, DefaultPropertyMapper.INSTANCE};
    private final PropertySource<?> propertySource;
    private final PropertyMapper[] mappers;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpringConfigurationPropertySource(PropertySource<?> propertySource, PropertyMapper... mappers) {
        Assert.notNull(propertySource, "PropertySource must not be null");
        Assert.isTrue(mappers.length > 0, "Mappers must contain at least one item");
        this.propertySource = propertySource;
        this.mappers = mappers;
    }

    @Override // org.springframework.boot.context.properties.source.ConfigurationPropertySource
    public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
        PropertyMapper[] propertyMapperArr;
        if (name == null) {
            return null;
        }
        for (PropertyMapper mapper : this.mappers) {
            try {
                for (String candidate : mapper.map(name)) {
                    Object value = getPropertySource().getProperty(candidate);
                    if (value != null) {
                        Origin origin = PropertySourceOrigin.get(getPropertySource(), candidate);
                        return ConfigurationProperty.of(name, value, origin);
                    }
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override // org.springframework.boot.context.properties.source.ConfigurationPropertySource
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        if (getPropertySource().getSource() instanceof Random) {
            return containsDescendantOfForRandom(name);
        }
        return ConfigurationPropertyState.UNKNOWN;
    }

    private static ConfigurationPropertyState containsDescendantOfForRandom(ConfigurationPropertyName name) {
        if (name.isAncestorOf(RANDOM) || name.equals(RANDOM)) {
            return ConfigurationPropertyState.PRESENT;
        }
        return ConfigurationPropertyState.ABSENT;
    }

    @Override // org.springframework.boot.context.properties.source.ConfigurationPropertySource
    public Object getUnderlyingSource() {
        return this.propertySource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PropertySource<?> getPropertySource() {
        return this.propertySource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final PropertyMapper[] getMappers() {
        return this.mappers;
    }

    public String toString() {
        return this.propertySource.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpringConfigurationPropertySource from(PropertySource<?> source) {
        Assert.notNull(source, "Source must not be null");
        PropertyMapper[] mappers = getPropertyMappers(source);
        if (isFullEnumerable(source)) {
            return new SpringIterableConfigurationPropertySource((EnumerablePropertySource) source, mappers);
        }
        return new SpringConfigurationPropertySource(source, mappers);
    }

    private static PropertyMapper[] getPropertyMappers(PropertySource<?> source) {
        if ((source instanceof SystemEnvironmentPropertySource) && hasSystemEnvironmentName(source)) {
            return SYSTEM_ENVIRONMENT_MAPPERS;
        }
        return DEFAULT_MAPPERS;
    }

    private static boolean hasSystemEnvironmentName(PropertySource<?> source) {
        String name = source.getName();
        return "systemEnvironment".equals(name) || name.endsWith("-systemEnvironment");
    }

    private static boolean isFullEnumerable(PropertySource<?> source) {
        PropertySource<?> rootSource = getRootSource(source);
        if (rootSource.getSource() instanceof Map) {
            try {
                ((Map) rootSource.getSource()).size();
            } catch (UnsupportedOperationException e) {
                return false;
            }
        }
        return source instanceof EnumerablePropertySource;
    }

    private static PropertySource<?> getRootSource(PropertySource<?> source) {
        while (source.getSource() != null && (source.getSource() instanceof PropertySource)) {
            source = (PropertySource) source.getSource();
        }
        return source;
    }
}
