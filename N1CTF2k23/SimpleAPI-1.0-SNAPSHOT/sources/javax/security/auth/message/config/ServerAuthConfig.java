package javax.security.auth.message.config;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/security/auth/message/config/ServerAuthConfig.class */
public interface ServerAuthConfig extends AuthConfig {
    ServerAuthContext getAuthContext(String str, Subject subject, Map map) throws AuthException;
}
