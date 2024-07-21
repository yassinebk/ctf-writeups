package javax.servlet.http;

import java.util.EventListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/HttpSessionIdListener.class */
public interface HttpSessionIdListener extends EventListener {
    void sessionIdChanged(HttpSessionEvent httpSessionEvent, String str);
}
