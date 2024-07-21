package org.springframework.boot.web.embedded.tomcat;

import org.apache.coyote.ProtocolHandler;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/TomcatProtocolHandlerCustomizer.class */
public interface TomcatProtocolHandlerCustomizer<T extends ProtocolHandler> {
    void customize(T protocolHandler);
}
