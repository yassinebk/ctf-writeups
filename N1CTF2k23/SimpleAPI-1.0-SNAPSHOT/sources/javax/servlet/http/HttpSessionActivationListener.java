package javax.servlet.http;

import java.util.EventListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/HttpSessionActivationListener.class */
public interface HttpSessionActivationListener extends EventListener {
    default void sessionWillPassivate(HttpSessionEvent se) {
    }

    default void sessionDidActivate(HttpSessionEvent se) {
    }
}
