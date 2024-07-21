package org.springframework.http.server.reactive;

import java.security.cert.X509Certificate;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/reactive/SslInfo.class */
public interface SslInfo {
    @Nullable
    String getSessionId();

    @Nullable
    X509Certificate[] getPeerCertificates();
}
