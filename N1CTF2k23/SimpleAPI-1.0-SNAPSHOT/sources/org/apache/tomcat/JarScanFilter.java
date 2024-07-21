package org.apache.tomcat;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/JarScanFilter.class */
public interface JarScanFilter {
    boolean check(JarScanType jarScanType, String str);

    default boolean isSkipAll() {
        return false;
    }
}
