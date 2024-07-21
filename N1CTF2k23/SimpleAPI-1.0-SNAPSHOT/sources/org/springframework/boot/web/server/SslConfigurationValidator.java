package org.springframework.boot.web.server;

import java.security.KeyStore;
import java.security.KeyStoreException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/server/SslConfigurationValidator.class */
public final class SslConfigurationValidator {
    private SslConfigurationValidator() {
    }

    public static void validateKeyAlias(KeyStore keyStore, String keyAlias) {
        if (!StringUtils.isEmpty(keyAlias)) {
            try {
                Assert.state(keyStore.containsAlias(keyAlias), () -> {
                    return String.format("Keystore does not contain specified alias '%s'", keyAlias);
                });
            } catch (KeyStoreException ex) {
                throw new IllegalStateException(String.format("Could not determine if keystore contains alias '%s'", keyAlias), ex);
            }
        }
    }
}
