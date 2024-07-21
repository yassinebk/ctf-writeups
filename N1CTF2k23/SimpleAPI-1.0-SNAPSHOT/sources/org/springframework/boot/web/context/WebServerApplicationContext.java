package org.springframework.boot.web.context;

import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/context/WebServerApplicationContext.class */
public interface WebServerApplicationContext extends ApplicationContext {
    WebServer getWebServer();

    String getServerNamespace();

    static boolean hasServerNamespace(ApplicationContext context, String serverNamespace) {
        return (context instanceof WebServerApplicationContext) && ObjectUtils.nullSafeEquals(((WebServerApplicationContext) context).getServerNamespace(), serverNamespace);
    }
}
