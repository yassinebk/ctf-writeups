package javax.security.auth.message;

import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/security/auth/message/MessageInfo.class */
public interface MessageInfo {
    Object getRequestMessage();

    Object getResponseMessage();

    void setRequestMessage(Object obj);

    void setResponseMessage(Object obj);

    Map getMap();
}
