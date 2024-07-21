package javax.security.auth.message;

import javax.security.auth.Subject;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/security/auth/message/ClientAuth.class */
public interface ClientAuth {
    AuthStatus secureRequest(MessageInfo messageInfo, Subject subject) throws AuthException;

    AuthStatus validateResponse(MessageInfo messageInfo, Subject subject, Subject subject2) throws AuthException;

    void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException;
}
