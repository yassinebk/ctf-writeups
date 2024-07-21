package javax.security.auth.message.config;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/security/auth/message/config/AuthConfigProvider.class */
public interface AuthConfigProvider {
    ClientAuthConfig getClientAuthConfig(String str, String str2, CallbackHandler callbackHandler) throws AuthException;

    ServerAuthConfig getServerAuthConfig(String str, String str2, CallbackHandler callbackHandler) throws AuthException;

    void refresh();
}
