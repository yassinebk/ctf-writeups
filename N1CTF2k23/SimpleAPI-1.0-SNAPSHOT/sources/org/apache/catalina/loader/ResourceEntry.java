package org.apache.catalina.loader;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/loader/ResourceEntry.class */
public class ResourceEntry {
    public long lastModified = -1;
    public volatile Class<?> loadedClass = null;
}
