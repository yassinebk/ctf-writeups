package org.springframework.core.env;

import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/env/ConfigurableEnvironment.class */
public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {
    void setActiveProfiles(String... strArr);

    void addActiveProfile(String str);

    void setDefaultProfiles(String... strArr);

    MutablePropertySources getPropertySources();

    Map<String, Object> getSystemProperties();

    Map<String, Object> getSystemEnvironment();

    void merge(ConfigurableEnvironment configurableEnvironment);
}
