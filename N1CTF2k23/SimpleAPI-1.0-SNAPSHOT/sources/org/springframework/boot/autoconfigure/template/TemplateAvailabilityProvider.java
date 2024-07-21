package org.springframework.boot.autoconfigure.template;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/template/TemplateAvailabilityProvider.class */
public interface TemplateAvailabilityProvider {
    boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader);
}
