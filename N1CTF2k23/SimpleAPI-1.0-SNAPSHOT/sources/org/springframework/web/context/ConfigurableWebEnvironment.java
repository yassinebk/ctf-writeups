package org.springframework.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/ConfigurableWebEnvironment.class */
public interface ConfigurableWebEnvironment extends ConfigurableEnvironment {
    void initPropertySources(@Nullable ServletContext servletContext, @Nullable ServletConfig servletConfig);
}
