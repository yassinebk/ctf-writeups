package org.apache.catalina;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/StoreManager.class */
public interface StoreManager extends DistributedManager {
    Store getStore();

    void removeSuper(Session session);
}
