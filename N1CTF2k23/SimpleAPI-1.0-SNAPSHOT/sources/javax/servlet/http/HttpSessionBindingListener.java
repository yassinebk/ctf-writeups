package javax.servlet.http;

import java.util.EventListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/HttpSessionBindingListener.class */
public interface HttpSessionBindingListener extends EventListener {
    default void valueBound(HttpSessionBindingEvent event) {
    }

    default void valueUnbound(HttpSessionBindingEvent event) {
    }
}
