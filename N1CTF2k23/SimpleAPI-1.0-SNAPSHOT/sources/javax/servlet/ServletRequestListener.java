package javax.servlet;

import java.util.EventListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/ServletRequestListener.class */
public interface ServletRequestListener extends EventListener {
    default void requestDestroyed(ServletRequestEvent sre) {
    }

    default void requestInitialized(ServletRequestEvent sre) {
    }
}
