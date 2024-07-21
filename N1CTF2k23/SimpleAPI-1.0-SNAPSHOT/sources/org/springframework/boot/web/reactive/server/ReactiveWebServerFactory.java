package org.springframework.boot.web.reactive.server;

import org.springframework.boot.web.server.WebServer;
import org.springframework.http.server.reactive.HttpHandler;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/reactive/server/ReactiveWebServerFactory.class */
public interface ReactiveWebServerFactory {
    WebServer getWebServer(HttpHandler httpHandler);
}
