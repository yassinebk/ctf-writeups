package org.apache.tomcat;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/ContextBind.class */
public interface ContextBind {
    ClassLoader bind(boolean z, ClassLoader classLoader);

    void unbind(boolean z, ClassLoader classLoader);
}
