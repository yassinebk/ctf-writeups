package org.springframework.boot.context.properties;

import org.springframework.boot.context.properties.bind.BindHandler;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBindHandlerAdvisor.class */
public interface ConfigurationPropertiesBindHandlerAdvisor {
    BindHandler apply(BindHandler bindHandler);
}
