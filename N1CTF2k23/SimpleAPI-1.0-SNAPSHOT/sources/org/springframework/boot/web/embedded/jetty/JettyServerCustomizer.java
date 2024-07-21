package org.springframework.boot.web.embedded.jetty;

import org.eclipse.jetty.server.Server;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/jetty/JettyServerCustomizer.class */
public interface JettyServerCustomizer {
    void customize(Server server);
}
