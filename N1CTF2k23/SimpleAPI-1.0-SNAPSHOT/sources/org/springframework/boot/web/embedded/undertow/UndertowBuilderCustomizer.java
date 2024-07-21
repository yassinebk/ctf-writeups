package org.springframework.boot.web.embedded.undertow;

import io.undertow.Undertow;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/UndertowBuilderCustomizer.class */
public interface UndertowBuilderCustomizer {
    void customize(Undertow.Builder builder);
}
