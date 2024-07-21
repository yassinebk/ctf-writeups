package org.springframework.boot.web.embedded.undertow;

import io.undertow.server.HttpHandler;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/HttpHandlerFactory.class */
public interface HttpHandlerFactory {
    HttpHandler getHandler(HttpHandler next);
}
