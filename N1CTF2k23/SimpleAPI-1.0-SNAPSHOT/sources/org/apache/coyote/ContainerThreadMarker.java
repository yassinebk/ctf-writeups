package org.apache.coyote;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/ContainerThreadMarker.class */
public class ContainerThreadMarker {
    public static boolean isContainerThread() {
        return org.apache.tomcat.util.net.ContainerThreadMarker.isContainerThread();
    }

    public static void set() {
        org.apache.tomcat.util.net.ContainerThreadMarker.set();
    }

    public static void clear() {
        org.apache.tomcat.util.net.ContainerThreadMarker.clear();
    }
}
