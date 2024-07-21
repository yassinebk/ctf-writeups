package org.springframework.boot.autoconfigure.session;

import org.springframework.session.web.http.DefaultCookieSerializer;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/session/DefaultCookieSerializerCustomizer.class */
public interface DefaultCookieSerializerCustomizer {
    void customize(DefaultCookieSerializer cookieSerializer);
}
