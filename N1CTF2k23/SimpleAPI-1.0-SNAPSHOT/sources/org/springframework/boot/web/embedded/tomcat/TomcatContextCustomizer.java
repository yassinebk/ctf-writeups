package org.springframework.boot.web.embedded.tomcat;

import org.apache.catalina.Context;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/TomcatContextCustomizer.class */
public interface TomcatContextCustomizer {
    void customize(Context context);
}
