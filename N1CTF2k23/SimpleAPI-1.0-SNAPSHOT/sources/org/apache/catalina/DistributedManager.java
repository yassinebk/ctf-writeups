package org.apache.catalina;

import java.util.Set;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/DistributedManager.class */
public interface DistributedManager {
    int getActiveSessionsFull();

    Set<String> getSessionIdsFull();
}
