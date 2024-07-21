package javax.servlet.http;

import java.util.EventListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/HttpSessionListener.class */
public interface HttpSessionListener extends EventListener {
    default void sessionCreated(HttpSessionEvent se) {
    }

    default void sessionDestroyed(HttpSessionEvent se) {
    }
}
