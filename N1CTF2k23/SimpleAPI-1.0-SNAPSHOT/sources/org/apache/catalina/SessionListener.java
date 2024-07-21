package org.apache.catalina;

import java.util.EventListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/SessionListener.class */
public interface SessionListener extends EventListener {
    void sessionEvent(SessionEvent sessionEvent);
}
