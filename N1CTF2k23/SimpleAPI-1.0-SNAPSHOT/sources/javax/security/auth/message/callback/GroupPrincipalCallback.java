package javax.security.auth.message.callback;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/security/auth/message/callback/GroupPrincipalCallback.class */
public class GroupPrincipalCallback implements Callback {
    private final Subject subject;
    private final String[] groups;

    public GroupPrincipalCallback(Subject subject, String[] groups) {
        this.subject = subject;
        this.groups = groups;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public String[] getGroups() {
        return this.groups;
    }
}
