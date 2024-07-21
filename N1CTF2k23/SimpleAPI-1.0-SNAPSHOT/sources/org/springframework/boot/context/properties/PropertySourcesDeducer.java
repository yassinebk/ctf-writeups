package org.springframework.boot.context.properties;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/PropertySourcesDeducer.class */
class PropertySourcesDeducer {
    private static final Log logger = LogFactory.getLog(PropertySourcesDeducer.class);
    private final ApplicationContext applicationContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PropertySourcesDeducer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PropertySources getPropertySources() {
        PropertySourcesPlaceholderConfigurer configurer = getSinglePropertySourcesPlaceholderConfigurer();
        if (configurer != null) {
            return configurer.getAppliedPropertySources();
        }
        MutablePropertySources sources = extractEnvironmentPropertySources();
        Assert.state(sources != null, "Unable to obtain PropertySources from PropertySourcesPlaceholderConfigurer or Environment");
        return sources;
    }

    private PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer() {
        Map<String, PropertySourcesPlaceholderConfigurer> beans = this.applicationContext.getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false, false);
        if (beans.size() == 1) {
            return beans.values().iterator().next();
        }
        if (beans.size() > 1 && logger.isWarnEnabled()) {
            logger.warn("Multiple PropertySourcesPlaceholderConfigurer beans registered " + beans.keySet() + ", falling back to Environment");
            return null;
        }
        return null;
    }

    private MutablePropertySources extractEnvironmentPropertySources() {
        Environment environment = this.applicationContext.getEnvironment();
        if (environment instanceof ConfigurableEnvironment) {
            return ((ConfigurableEnvironment) environment).getPropertySources();
        }
        return null;
    }
}
