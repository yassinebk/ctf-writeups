package javax.security.auth.message;

import javax.security.auth.Subject;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/security/auth/message/ServerAuth.class */
public interface ServerAuth {
    AuthStatus validateRequest(MessageInfo messageInfo, Subject subject, Subject subject2) throws AuthException;

    AuthStatus secureResponse(MessageInfo messageInfo, Subject subject) throws AuthException;

    void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException;
}
