package org.springframework.boot.web.server;

import java.security.KeyStore;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/server/SslStoreProvider.class */
public interface SslStoreProvider {
    KeyStore getKeyStore() throws Exception;

    KeyStore getTrustStore() throws Exception;
}
