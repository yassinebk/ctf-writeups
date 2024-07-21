package org.springframework.boot.autoconfigure.web.reactive;

import org.springframework.web.reactive.config.ResourceHandlerRegistration;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/ResourceHandlerRegistrationCustomizer.class */
public interface ResourceHandlerRegistrationCustomizer {
    void customize(ResourceHandlerRegistration registration);
}
