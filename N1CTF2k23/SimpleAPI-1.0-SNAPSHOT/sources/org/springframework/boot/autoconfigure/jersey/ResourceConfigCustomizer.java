package org.springframework.boot.autoconfigure.jersey;

import org.glassfish.jersey.server.ResourceConfig;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jersey/ResourceConfigCustomizer.class */
public interface ResourceConfigCustomizer {
    void customize(ResourceConfig config);
}
