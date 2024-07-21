package org.apache.coyote;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/AsyncContextCallback.class */
public interface AsyncContextCallback {
    void fireOnComplete();

    boolean isAvailable();
}
